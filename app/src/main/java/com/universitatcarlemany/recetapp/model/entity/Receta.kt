package com.universitatcarlemany.recetapp.model.entity

data class Receta(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val foto: String,
    val ingredientes: List<String>,
    val preparacion: String
)
