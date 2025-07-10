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

        // Get recipe ID passed from the previous activity
        val recetaId = intent.getIntExtra("receta_id", -1)

        // Load recipes from JSON and find the selected one by ID
        val receta = cargarRecetasDesdeAssets().find { it.id == recetaId }

        receta?.let {
            // Set recipe name, category and description
            findViewById<TextView>(R.id.txtDetalleNombre).text = it.nombre
            findViewById<TextView>(R.id.txtDetalleCategoria).text = it.categoria
            findViewById<TextView>(R.id.txtDetalleDescripcion).text = it.descripcion

            // Show ingredients as bullet points
            val ingredientesText = it.ingredientes.joinToString(separator = "\n") { ing -> "• $ing" }
            findViewById<TextView>(R.id.txtDetalleIngredientes).text = ingredientesText

            // Set preparation steps
            findViewById<TextView>(R.id.txtDetallePreparacion).text = it.preparacion

            // Set recipe image, or placeholder if not found
            val fotoResId = resources.getIdentifier(it.foto, "drawable", packageName)
            findViewById<ImageView>(R.id.imgDetalleFoto).setImageResource(
                if (fotoResId != 0) fotoResId else R.drawable.ic_launcher_background
            )
        }
    }

    // Loads all recipes from assets/recetas.json
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
