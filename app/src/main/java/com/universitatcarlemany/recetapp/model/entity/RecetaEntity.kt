package com.universitatcarlemany.recetapp.model.entity.sampledata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class RecetaEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val foto: String
)


