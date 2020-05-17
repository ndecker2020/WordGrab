package com.ndecker.android.wordgrab.retrofit

import com.ndecker.android.wordgrab.model.WordsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

//TODO register for api
//i subscribed to the service, please don't exceed 1200 requests per day
private const val api_key = "5be77fdc93mshfa53e1dfc646031p12ff56jsn230f01be791f"

interface WordsApi {
    @Headers("x-rapidapi-key: 5be77fdc93mshfa53e1dfc646031p12ff56jsn230f01be791f", "x-rapidapi-host: wordsapiv1.p.rapidapi.com", "useQueryString: true")
    @GET("words/{word}/definitions")
    fun getDefinitions(@Path("word") word: String): Call<WordsApiResponse>
}