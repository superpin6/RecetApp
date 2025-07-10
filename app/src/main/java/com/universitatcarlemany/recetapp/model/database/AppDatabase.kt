package com.universitatcarlemany.recetapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.universitatcarlemany.recetapp.model.dao.FavoriteRecipeDao
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity

// Room Database class, stores favorite recipes
@Database(entities = [FavoriteRecipeEntity::class], version = 2) // Increase version if you change fields!
@TypeConverters(StringListConverter::class) // Converts lists to store in Room
abstract class AppDatabase : RoomDatabase() {

    // Returns the DAO for favorite recipes
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
}
