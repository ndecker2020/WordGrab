package com.ndecker.android.wordgrab

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Word(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var word: String,
                var category: String)