package com.universitatcarlemany.recetapp.model.database

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString("||") ?: ""
    }
    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split("||")?.filter { it.isNotEmpty() } ?: emptyList()
    }
}
