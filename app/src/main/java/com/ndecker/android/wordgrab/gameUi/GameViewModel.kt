package com.ndecker.android.wordgrab.gameUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.categoryDatabase.CategoryRepository

class GameViewModel:ViewModel() {
    private val categoryRepository = CategoryRepository.get()
    val categoriesLiveData = categoryRepository.getCategories()
    var selectedScore = 0;
    var selectedCategory = 0;

    var categoryLiveData: LiveData<List<Category>> =
        Transformations.switchMap(categoriesLiveData){
            categoryID->categoryRepository.getCategories()
        }
}
