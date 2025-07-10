package com.universitatcarlemany.recetapp.model.dao

import androidx.room.*
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity

@Dao
interface FavoriteRecipeDao {

    // Get all favorite recipes from the database
    @Query("SELECT * FROM FavoriteRecipeEntity")
    suspend fun getAll(): List<FavoriteRecipeEntity>

    // Insert a recipe as favorite (replace if it already exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: FavoriteRecipeEntity)

    // Remove a recipe from favorites
    @Delete
    suspend fun delete(recipe: FavoriteRecipeEntity)

    // Get a favorite recipe by its ID
    @Query("SELECT * FROM FavoriteRecipeEntity WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FavoriteRecipeEntity?
}
