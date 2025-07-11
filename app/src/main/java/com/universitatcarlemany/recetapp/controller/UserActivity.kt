package com.universitatcarlemany.recetapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    // ViewBinding for accessing layout views safely
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the default ActionBar title to use the custom Toolbar title.
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Open RecetarioActivity when the "Recetario" button is clicked
        binding.btnRecetario.setOnClickListener {
            startActivity(Intent(this, RecetarioActivity::class.java))    //
        }

        // Open FavoritesActivity when the "Favoritos" button is clicked
        binding.btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // Open DatosPrivadosActivity when the user icon is clicked
        binding.imgUserIcon.setOnClickListener {
            startActivity(Intent(this, DatosPrivadosActivity::class.java))
        }
    }
}
