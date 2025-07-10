package com.universitatcarlemany.recetapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity representing a favorite recipe in the Room database
@Entity
data class FavoriteRecipeEntity(
    @PrimaryKey val id: Int,           // Unique ID for each recipe
    val nombre: String,                // Recipe name
    val descripcion: String,           // Recipe description
    val categoria: String,             // Recipe category
    val foto: String,                  // Name of the image resource
    val ingredientes: List<String>,    // List of ingredients (stored as String by converter)
    val preparacion: String            // Preparation steps
)
