package com.universitatcarlemany.recetapp.database

import android.content.Context
import androidx.room.Room
import com.universitatcarlemany.recetapp.model.database.AppDatabase

// Singleton object to provide a single instance of AppDatabase
object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    // Returns the singleton database instance
    fun getDatabase(context: Context): AppDatabase {
        // Double-checked locking to ensure thread safety
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "recetapp-db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
