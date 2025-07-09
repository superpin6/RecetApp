package com.universitatcarlemany.recetapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.universitatcarlemany.recetapp.R


object NotificationHelper {
    private const val CHANNEL_ID = "favoritos_channel"

    fun showFavoriteAdded(context: Context, recetaNombre: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Favoritos",
                NotificationManager.IMPORTANCE_HIGH // Cambiado a HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Añadida a favoritos")
            .setContentText("La receta \"$recetaNombre\" se ha añadido a favoritos.")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Importante
            .setDefaults(NotificationCompat.DEFAULT_ALL)    // Vibración y sonido
            .build()

        manager.notify(1001, notification)
    }
}
