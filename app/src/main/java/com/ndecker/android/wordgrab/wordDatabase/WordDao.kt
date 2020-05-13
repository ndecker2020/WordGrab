package com.ndecker.android.wordgrab.wordDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ndecker.android.wordgrab.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM word WHERE category=(:category)")
    fun getWords(category: String): LiveData<List<Word>>

    @Query("SELECT * FROM word WHERE word=(:word) ")
    fun getWord(word: String) : Word?

    @Insert
    fun addWord(word: Word)

}