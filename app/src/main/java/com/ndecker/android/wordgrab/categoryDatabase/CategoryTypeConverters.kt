package com.ndecker.android.wordgrab.categoryDatabase

import androidx.room.TypeConverter
import java.util.*

class CategoryTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}