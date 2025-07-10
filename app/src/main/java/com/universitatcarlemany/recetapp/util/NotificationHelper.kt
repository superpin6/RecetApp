package com.universitatcarlemany.recetapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.universitatcarlemany.recetapp.R
import com.universitatcarlemany.recetapp.controller.RecetarioActivity

object NotificationHelper {
    private const val CHANNEL_ID = "favoritos_channel"
    private const val CHANNEL_NAME = "Favoritos"
    private const val CHANNEL_DESC = "Notifications for favorite recipes"

    // Call this only once (for example, in your activity's onCreate)
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

    // Show a notification when a recipe is added to favorites
    fun showFavoriteAdded(context: Context, recetaNombre: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // --- Use TaskStackBuilder to create the correct back stack for navigation ---
        val resultIntent = Intent(context, RecetarioActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(resultIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_estrella_favorito)  // Use your star icon here
            .setContentTitle("Added to Favorites")
            .setContentText("The recipe \"$recetaNombre\" was added to favorites.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // <-- This ensures correct back navigation
            .build()

        manager.notify(1001, notification)
    }
}
