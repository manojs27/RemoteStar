package com.example.videofeed.network

import com.example.videofeed.model.VideoData
import retrofit2.Call
import retrofit2.http.*

interface SosService{

    @Headers("Content-Type: application/json")
    @GET("/v3/dfe94561-e26a-405b-9813-516b20d86fe1")
    fun getVids(): Call<VideoData>
}