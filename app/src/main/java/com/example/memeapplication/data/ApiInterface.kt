package com.example.memeapplication.data

import com.example.memeapplication.models.GetResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("get_memes")
    suspend fun getMemesList() : Response<GetResponse>
}