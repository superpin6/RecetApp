package com.universitatcarlemany.recetapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.universitatcarlemany.recetapp.model.dao.FavoriteRecipeDao
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity

@Database(entities = [FavoriteRecipeEntity::class], version = 2) // SUBE la versión al cambiar campos
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
}
