package com.universitatcarlemany.recetapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar el título por defecto del ActionBar para dejar solo el personalizado
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Botón para ver el recetario
        binding.btnRecetario.setOnClickListener {
            startActivity(Intent(this, RecetarioActivity::class.java))
        }

        // Botón para favoritos
        binding.btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // Icono usuario (arriba derecha)
        binding.imgUserIcon.setOnClickListener {
            startActivity(Intent(this, DatosPrivadosActivity::class.java))
        }
    }
}
