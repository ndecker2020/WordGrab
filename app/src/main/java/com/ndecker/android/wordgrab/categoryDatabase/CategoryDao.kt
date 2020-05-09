package com.ndecker.android.wordgrab.categoryDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ndecker.android.wordgrab.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): MutableList<Category>

    @Query("SELECT * FROM category WHERE name=(:name)")
    fun getCategory(name: String): Category?

    @Insert
    fun addCategory(name: Category)
}