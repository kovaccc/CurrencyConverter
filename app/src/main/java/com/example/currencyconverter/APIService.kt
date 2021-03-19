package com.example.currencyconverter

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

interface APIService {

    @GET(".") // final URL is the same as base URL that is why we put .
    suspend fun getCurrency(@Query("date") date: String): Response<ResponseBody>


    companion object { // companion objects are singleton, you can access them without creating instance of a class
        fun create() : APIService {
            // Create Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("http://hnbex.eu/api/v1/rates/daily/")
                .build()

            // Create Service
            return retrofit.create(APIService::class.java)
        }
    }
}


