package com.universitatcarlemany.recetapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.universitatcarlemany.recetapp.R
import com.universitatcarlemany.recetapp.controller.RecetarioActivity

object NotificationHelper {
    private const val CHANNEL_ID = "favoritos_channel"
    private const val CHANNEL_NAME = "Favoritos"
    private const val CHANNEL_DESC = "Notificaciones de recetas favoritas"

    // Solo se llama una vez (por ejemplo, en el onCreate de tu activity)
    fun createFavoritosChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                enableLights(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showFavoriteAdded(context: Context, recetaNombre: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // --- ¡IMPORTANTE! PendingIntent para que al pulsar, abra RecetarioActivity ---
        val intent = Intent(context, RecetarioActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_estrella_favorito)  // Pon aquí tu icono de estrella
            .setContentTitle("Añadida a favoritos")
            .setContentText("La receta \"$recetaNombre\" se ha añadido a favoritos.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)   // Aquí está la clave
            .build()

        manager.notify(1001, notification)
    }
}
