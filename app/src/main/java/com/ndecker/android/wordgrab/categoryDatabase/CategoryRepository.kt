package com.ndecker.android.wordgrab.categoryDatabase

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ndecker.android.wordgrab.Category
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class CategoryRepository private constructor(context: Context){
    private val database: CategoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        CategoryDatabase::class.java,
        "category-database")
        .build()

    private val categoryDao = database.categoryDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getCategories() : LiveData<List<Category>> = categoryDao.getCategories()

    fun getCategory(name: String) : LiveData<Category> = categoryDao.getCategory(name)

    fun addCategory(category: Category){
        GlobalScope.launch {
            categoryDao.addCategory(category)
        }
    }

    fun deleteCategory(category: Category){
        GlobalScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    companion object{
        private var INSTANCE : CategoryRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CategoryRepository(context)
            }
        }

        fun get(): CategoryRepository{
            return INSTANCE ?: throw IllegalStateException("CategoryRepository must be initialized first")
        }
    }
}