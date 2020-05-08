package com.ndecker.android.wordgrab.wordDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ndecker.android.wordgrab.Word

@Database(entities = [ Word::class], version= 1)
@TypeConverters(WordTypeConverters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

}