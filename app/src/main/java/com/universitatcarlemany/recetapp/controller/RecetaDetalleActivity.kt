package com.universitatcarlemany.recetapp.controller

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.universitatcarlemany.recetapp.R
import com.universitatcarlemany.recetapp.model.entity.Receta

class RecetaDetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receta_detalle)

        val recetaId = intent.getIntExtra("receta_id", -1)
        val receta = cargarRecetasDesdeAssets().find { it.id == recetaId }

        receta?.let {
            findViewById<TextView>(R.id.txtDetalleNombre).text = it.nombre
            findViewById<TextView>(R.id.txtDetalleCategoria).text = it.categoria
            findViewById<TextView>(R.id.txtDetalleDescripcion).text = it.descripcion

            // Ingredientes (un string con salto de línea por ingrediente)
            val ingredientesText = it.ingredientes.joinToString(separator = "\n") { ing -> "• $ing" }
            findViewById<TextView>(R.id.txtDetalleIngredientes).text = ingredientesText

            // Preparación
            findViewById<TextView>(R.id.txtDetallePreparacion).text = it.preparacion

            // Imagen
            val fotoResId = resources.getIdentifier(it.foto, "drawable", packageName)
            findViewById<ImageView>(R.id.imgDetalleFoto).setImageResource(
                if (fotoResId != 0) fotoResId else R.drawable.ic_launcher_background
            )
        }
    }

    // Puedes mover esta función a un util si la usas mucho
    private fun cargarRecetasDesdeAssets(): List<Receta> {
        return try {
            val json = assets.open("recetas.json")
                .bufferedReader()
                .use { it.readText() }
            val tipoLista = object : TypeToken<List<Receta>>() {}.type
            Gson().fromJson(json, tipoLista)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
