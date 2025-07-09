package com.universitatcarlemany.recetapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteRecipeEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val foto: String,
    val ingredientes: List<String>,  // Nuevo campo
    val preparacion: String    // Nuevo campo
)
