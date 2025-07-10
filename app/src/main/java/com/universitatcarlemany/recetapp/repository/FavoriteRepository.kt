package com.universitatcarlemany.recetapp.repository

import com.universitatcarlemany.recetapp.model.database.AppDatabase
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository for managing favorite recipes with Room database
class FavoriteRepository(private val db: AppDatabase) {
    private val favoriteDao = db.favoriteRecipeDao()

    // Adds a recipe to favorites
    suspend fun addFavorite(recipe: FavoriteRecipeEntity) = withContext(Dispatchers.IO) {
        favoriteDao.insert(recipe)
    }

    // Removes a recipe from favorites
    suspend fun removeFavorite(recipe: FavoriteRecipeEntity) = withContext(Dispatchers.IO) {
        favoriteDao.delete(recipe)
    }

    // Gets the list of all favorite recipes
    suspend fun getAllFavorites(): List<FavoriteRecipeEntity> = withContext(Dispatchers.IO) {
        favoriteDao.getAll()
    }

    // Checks if a recipe is already a favorite by its ID
    suspend fun isFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        favoriteDao.getById(id) != null
    }
}
