package com.example.dysnatyinterview.services

import com.example.dysnatyinterview.model.Listing
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface APIServices {
    @Headers("Content-Type: application/json")
    @GET("v2/7f179593/articles/{pageNumber}")
    suspend fun getArticleListings(@Path("pageNumber") pageNumber: Int): Listing
}