package com.universitatcarlemany.recetapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.controller.UserActivity
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Puedes poner aquí un layout sencillo de splash si quieres, por ejemplo:
        // setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }, 1000) // 1000 ms = 1 segundo. Cambia el valor si quieres.
    }
}
