package com.universitatcarlemany.recetapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.recetapp.R
import com.universitatcarlemany.recetapp.model.entity.Receta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecetaAdapter(
    private var recetas: List<Receta>,
    private val isFavorite: suspend (Int) -> Boolean,
    private val onToggleFavorite: suspend (Receta, Int) -> Unit, // Passes the position!
    private val onRecetaClick: (Receta) -> Unit
) : RecyclerView.Adapter<RecetaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreReceta)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcionReceta)
        val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
        val imgFoto: ImageView = itemView.findViewById(R.id.imgFotoReceta)
        val btnFavorito: Button = itemView.findViewById(R.id.btnFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = recetas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receta = recetas[position]
        holder.txtNombre.text = receta.nombre
        holder.txtDescripcion.text = receta.descripcion
        holder.txtCategoria.text = receta.categoria

        // Load image from drawable by name (if not found, use default)
        val context = holder.itemView.context
        val fotoResId = context.resources.getIdentifier(
            receta.foto, "drawable", context.packageName
        )
        holder.imgFoto.setImageResource(
            if (fotoResId != 0) fotoResId else R.drawable.ic_launcher_background
        )
        // Set favorite star async (Room check is suspend)
        CoroutineScope(Dispatchers.Main).launch {

            val favorito = isFavorite(receta.id)     //
            holder.btnFavorito.text = if (favorito) "★" else "☆"
            holder.btnFavorito.setTextColor(
                if (favorito) Color.parseColor("#FFA000") else Color.parseColor("#BBBBBB")
            )
        }


        // Only this star will refresh!
        holder.btnFavorito.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                onToggleFavorite(receta, position)
                notifyItemChanged(position)
            }
        }

        // Open recipe details on card click
        holder.itemView.setOnClickListener {
            onRecetaClick(receta)
        }
    }

    // Replace the current recipe list with a new one from an external source
    fun updateData(newRecetas: List<Receta>) {
        this.recetas = newRecetas
        notifyDataSetChanged()
    }
}
