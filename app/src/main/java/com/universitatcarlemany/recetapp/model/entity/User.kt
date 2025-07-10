package com.universitatcarlemany.recetapp.model.entity.sampledata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class representing a user. Implements Parcelable for passing between activities/fragments.
@Parcelize
data class User(
    val nombre: String,       // User's first name
    val apellido: String,     // User's last name
    val email: String,        // User's email address
    val telefono: String,     // User's phone number
    val direccion: String,    // User's address
    val fotoUrl: String       // URL or name of user's profile photo
) : Parcelable
