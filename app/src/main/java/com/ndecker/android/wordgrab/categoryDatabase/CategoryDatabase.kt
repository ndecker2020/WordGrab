package com.ndecker.android.wordgrab.categoryDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ndecker.android.wordgrab.Category

@Database(entities = [Category::class], version = 1)
@TypeConverters(CategoryTypeConverters::class)
abstract class CategoryDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}