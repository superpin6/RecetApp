package com.universitatcarlemany.recetapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.universitatcarlemany.recetapp.controller.UserActivity
import android.os.Handler
import android.os.Looper

// MainActivity used as a splash screen before starting the main UserActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optionally, you can show a splash layout here
        // setContentView(R.layout.activity_splash)

        // Delayed transition to UserActivity after 1 second
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }, 1000) // 1000 ms = 1 second; change this value if you want a longer splash
    }
}
