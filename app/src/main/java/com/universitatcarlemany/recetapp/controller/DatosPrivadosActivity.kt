package com.universitatcarlemany.recetapp.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.databinding.ActivityDatosPrivadosBinding

class DatosPrivadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatosPrivadosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosPrivadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aquí pones los datos privados que quieras mostrar
    }
}
