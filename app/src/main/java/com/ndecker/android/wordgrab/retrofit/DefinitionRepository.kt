package com.ndecker.android.wordgrab.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ndecker.android.wordgrab.Word
import com.ndecker.android.wordgrab.model.WordsApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "base url to the api"

class DefinitionRepository {
    private val wordsApi: WordsApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        wordsApi = retrofit.create(WordsApi::class.java)
    }

    fun getDefintions(word: String): LiveData<WordsApiResponse>{
        val responseLiveData: MutableLiveData<WordsApiResponse> = MutableLiveData()
        val definitionCall: Call<WordsApiResponse> = wordsApi.getDefinitions(word)

        definitionCall.enqueue(object: Callback<WordsApiResponse>{
            override fun onFailure(call: Call<WordsApiResponse>, t: Throwable) {

                //TODO return some error definition response

                Log.e("DefinitionRepository", "Error fetching definitions")
            }

            override fun onResponse(
                call: Call<WordsApiResponse>,
                response: Response<WordsApiResponse>) {
                val wordsApiResponse: WordsApiResponse? = response.body()

                //TODO check if response succeeds but is null
                //could have 0 definitions

                responseLiveData.value = wordsApiResponse
            }

        })

        return responseLiveData
    }
}