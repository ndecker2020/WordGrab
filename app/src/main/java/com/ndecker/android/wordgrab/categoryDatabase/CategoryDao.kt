package com.ndecker.android.wordgrab.categoryDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ndecker.android.wordgrab.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE name=(:name)")
    fun getCategory(name: String): LiveData<Category>

    @Insert
    fun addCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}