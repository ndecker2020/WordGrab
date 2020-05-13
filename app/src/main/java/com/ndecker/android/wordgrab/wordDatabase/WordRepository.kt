package com.ndecker.android.wordgrab.wordDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ndecker.android.wordgrab.Word
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "word-database"

class WordRepository private constructor(context: Context) {
    private val database : WordDatabase = Room.databaseBuilder(context.applicationContext, WordDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val wordDao = database.wordDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getWords(category: String): LiveData<List<Word>> = wordDao.getWords(category)

    fun getWord(word: String): Word? = wordDao.getWord(word)

    fun addWord(word: Word) {
        GlobalScope.launch {
            wordDao.addWord(word)
        }
    }

    companion object {
        private var INSTANCE: WordRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE =
                    WordRepository(
                        context
                    )
            }
        }

        fun get(): WordRepository {
            return INSTANCE
                ?: throw IllegalStateException("WordRepository must be initialized")
        }
    }
}