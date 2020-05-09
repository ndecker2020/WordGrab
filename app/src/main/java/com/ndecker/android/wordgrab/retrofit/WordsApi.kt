package com.ndecker.android.wordgrab.retrofit

import com.ndecker.android.wordgrab.model.WordsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//TODO register for api
private const val api_key = "someone needs to register"

interface WordsApi {
    @GET("words/{word}/definitions")
    fun getDefinitions(@Path("word") word: String): Call<WordsApiResponse>
}