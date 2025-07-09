package com.universitatcarlemany.recetapp.repository

import com.universitatcarlemany.recetapp.model.database.AppDatabase
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(private val db: AppDatabase) {
    private val favoriteDao = db.favoriteRecipeDao()

    suspend fun addFavorite(recipe: FavoriteRecipeEntity) = withContext(Dispatchers.IO) {
        favoriteDao.insert(recipe)
    }

    suspend fun removeFavorite(recipe: FavoriteRecipeEntity) = withContext(Dispatchers.IO) {
        favoriteDao.delete(recipe)
    }

    suspend fun getAllFavorites(): List<FavoriteRecipeEntity> = withContext(Dispatchers.IO) {
        favoriteDao.getAll()
    }

    suspend fun isFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        favoriteDao.getById(id) != null
    }
}

