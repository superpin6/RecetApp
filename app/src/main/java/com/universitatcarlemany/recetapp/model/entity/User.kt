package com.universitatcarlemany.recetapp.model.entity.sampledata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val fotoUrl: String
) : Parcelable
