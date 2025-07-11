package com.universitatcarlemany.recetapp.model.entity

// Data class representing a recipe loaded from JSON
data class Receta(
    val id: Int,                     // Unique recipe ID.
    val nombre: String,              // Recipe name.
    val descripcion: String,         // Recipe description.
    val categoria: String,           // Category (e.g., Fish, Dessert).
    val foto: String,                // Image file name (without extension).
    val ingredientes: List<String>,  // List of ingredients.
    val preparacion: String          // Preparation steps.
)
