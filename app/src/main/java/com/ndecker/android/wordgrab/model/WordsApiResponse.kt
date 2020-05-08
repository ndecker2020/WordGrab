package com.ndecker.android.wordgrab.model

//modeled after a simple response from www.wordsapi.com
//words/dog/definitions
//returns json with the word and a list of definitions
//each definition gives the explanation

//can return without any results.

data class WordsApiResponse (
    val word: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val partOfSpeech: String
)

