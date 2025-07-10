package com.universitatcarlemany.recetapp.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    // ViewBinding for safe and easy access to layout views
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Here you can set up click listeners or UI logic for menu buttons
        // Example:
        // binding.btnRecetario.setOnClickListener { ... }
        // binding.btnFavoritos.setOnClickListener { ... }
    }
}
