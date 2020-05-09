package com.ndecker.android.wordgrab.ui.main

import androidx.lifecycle.ViewModel
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.categoryDatabase.CategoryRepository

class MainViewModel : ViewModel() {
    private val categoryRepository = CategoryRepository.get()
    val categoriesLiveData = categoryRepository.getCategories()
}
