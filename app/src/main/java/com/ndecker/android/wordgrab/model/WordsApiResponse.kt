package com.ndecker.android.wordgrab.model

import com.ndecker.android.wordgrab.Word

//modeled after a simple response from www.wordsapi.com
//words/dog/definitions
//returns json with the word and a list of definitions
//each definition gives the explanation

//can return without any results.

data class WordsApiResponse (

    val word: String,
    val definitions: List<Definition>
){
    companion object{
        fun errorResponse(): WordsApiResponse{
            return WordsApiResponse("error", listOf<Definition>(Definition("Error getting defition", "Error")))
        }
    }
}

data class Definition(
    val definition: String,
    val partOfSpeech: String
)

