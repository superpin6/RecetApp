package com.universitatcarlemany.recetapp.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.databinding.ActivityDatosPrivadosBinding

class DatosPrivadosActivity : AppCompatActivity() {

    // ViewBinding for accessing the layout views
    private lateinit var binding: ActivityDatosPrivadosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate and bind the layout
        binding = ActivityDatosPrivadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // All user data is set directly in the layout file
        // If you want to load user data dynamically, you can set it here
        // Example:
        // binding.txtNombre.text = "Name: Julio López"
        // binding.txtEmail.text = "Email: ejemplo@correo.com"
    }
}
