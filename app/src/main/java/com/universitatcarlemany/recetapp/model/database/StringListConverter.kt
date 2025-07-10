package com.universitatcarlemany.recetapp.model.database

import androidx.room.TypeConverter

// Converts List<String> to String and back for Room database storage
class StringListConverter {

    // Converts a list to a single string, separated by "||"
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString("||") ?: ""
    }

    // Converts a string back to a list, splitting by "||"
    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split("||")?.filter { it.isNotEmpty() } ?: emptyList()
    }
}
