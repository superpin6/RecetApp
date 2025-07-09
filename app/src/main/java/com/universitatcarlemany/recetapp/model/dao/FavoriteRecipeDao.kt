package com.universitatcarlemany.recetapp.model.dao

import androidx.room.*
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT * FROM FavoriteRecipeEntity")
    suspend fun getAll(): List<FavoriteRecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: FavoriteRecipeEntity)

    @Delete
    suspend fun delete(recipe: FavoriteRecipeEntity)

    @Query("SELECT * FROM FavoriteRecipeEntity WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FavoriteRecipeEntity?
}
