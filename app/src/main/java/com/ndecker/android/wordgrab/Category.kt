package com.ndecker.android.wordgrab

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Category (@PrimaryKey val id: UUID = UUID.randomUUID(),
                     var name: String)