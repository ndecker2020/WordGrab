package com.ndecker.android.wordgrab.customList

import androidx.lifecycle.ViewModel
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.Word
import com.ndecker.android.wordgrab.categoryDatabase.CategoryRepository
import com.ndecker.android.wordgrab.wordDatabase.WordRepository

class CustomListViewModel : ViewModel() {
    private val wordRepository = WordRepository.get()
    private val categoryRepository = CategoryRepository.get()
    val categoriesLiveData = categoryRepository.getCategories()
    var selectedCategory = 0

    fun addWord(word: Word){
        wordRepository.addWord(word)
    }

    fun addCategory(category : Category) {
        categoryRepository.addCategory(category)
    }
}
