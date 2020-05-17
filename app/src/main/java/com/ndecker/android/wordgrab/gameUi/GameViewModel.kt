package com.ndecker.android.wordgrab.gameUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ndecker.android.wordgrab.Category
import com.ndecker.android.wordgrab.Word
import com.ndecker.android.wordgrab.categoryDatabase.CategoryRepository
import com.ndecker.android.wordgrab.retrofit.DefinitionRepository
import com.ndecker.android.wordgrab.wordDatabase.WordRepository

class GameViewModel:ViewModel() {
    private val categoryRepository = CategoryRepository.get()
    val categoriesLiveData = categoryRepository.getCategories()
    var selectedScore = 0
    var selectedCategory = 0
    var selectedCategoryString = ""
    private val wordRepository = WordRepository.get()
    lateinit var wordListLiveData: LiveData<List<Word>>

    private val definitionRepository = DefinitionRepository()

    private val hintWordLiveData = MutableLiveData<String>();
    val hintLiveData = Transformations.switchMap(hintWordLiveData){
        word -> definitionRepository.getDefinitions(word)
    }

    var currentWord = ""

    var categoryLiveData: LiveData<List<Category>> =
        Transformations.switchMap(categoriesLiveData){
            categoryID->categoryRepository.getCategories()
        }

    fun getWords(category: String) {
        wordListLiveData = wordRepository.getWords(category)
        Transformations.switchMap(wordListLiveData) {
            wordID -> wordRepository.getWords(category)
        }
    }

    fun loadDefinition(word: String){
        hintWordLiveData.value = word
    }

    fun clearDefinition(){
        hintWordLiveData.value = ""
    }
}
