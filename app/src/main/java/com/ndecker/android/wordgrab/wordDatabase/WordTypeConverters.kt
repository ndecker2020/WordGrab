package com.ndecker.android.wordgrab.wordDatabase

import java.util.*
import androidx.room.TypeConverter

class WordTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}