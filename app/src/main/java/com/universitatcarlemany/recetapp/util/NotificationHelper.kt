package com.universitatcarlemany.recetapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.universitatcarlemany.recetapp.R

// Utility object for creating and showing notifications for favorite recipes
object NotificationHelper {
    private const val CHANNEL_ID = "favoritos_channel"
    private const val CHANNEL_NAME = "Favoritos"
    private const val CHANNEL_DESC = "Notifications for favorite recipes"

    // Creates the notification channel (should be called once, e.g. in onCreate)
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

    // Shows a notification and closes it automatically after 5 seconds
    fun showFavoriteAdded(context: Context, recetaNombre: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1001
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_estrella_favorito)  // Use your custom icon
            .setContentTitle("Added to Favorites")
            .setContentText("The recipe \"$recetaNombre\" was added to favorites.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        manager.notify(notificationId, notification)

        // Automatically close the notification after 5 seconds
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            manager.cancel(notificationId)
        }, 5000)
    }
}
