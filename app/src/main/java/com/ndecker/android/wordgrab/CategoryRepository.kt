package com.ndecker.android.wordgrab

import android.content.Context
import androidx.room.Room
import com.ndecker.android.wordgrab.categoryDatabase.CategoryDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "category-database"

class CategoryRepository private constructor(context: Context) {
    private val database: CategoryDatabase = Room.databaseBuilder(context.applicationContext, CategoryDatabase::class.java, DATABASE_NAME).build()

    private val categoryDao = database.categoryDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getCategories(): MutableList<Category> = categoryDao.getCategories()

    fun getCategory(category: String): Category? = categoryDao.getCategory(category)

    fun addCategory(category: Category) {
        executor.execute {
            categoryDao.addCategory(category)
        }
    }

    companion object {
        private var INSTANCE: CategoryRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CategoryRepository(context)
            }
        }

        fun get(): CategoryRepository {
            return INSTANCE ?: throw IllegalStateException("CategoryRepository must be initialized")
        }
    }
}