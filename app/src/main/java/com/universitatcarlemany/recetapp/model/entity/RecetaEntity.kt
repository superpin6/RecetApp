package com.universitatcarlemany.recetapp.model.entity.sampledata

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room entity for storing recipes in the database
@Entity(tableName = "recetas")
data class RecetaEntity(
    @PrimaryKey val id: Int,     // Unique recipe ID
    val nombre: String,          // Recipe name
    val descripcion: String,     // Short description
    val categoria: String,       // Recipe category
    val foto: String             // Image file name
)
