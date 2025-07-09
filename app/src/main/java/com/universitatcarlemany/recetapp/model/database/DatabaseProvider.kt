package com.universitatcarlemany.recetapp.database

import android.content.Context
import androidx.room.Room
import com.universitatcarlemany.recetapp.model.database.AppDatabase

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
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
