package com.example.dysnatyinterview.repository

import android.util.Log
import com.example.dysnatyinterview.model.Article
import com.example.dysnatyinterview.model.Listing
import com.example.dysnatyinterview.services.APIServices
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject

class ListingRepository(private val apiServices: APIServices) {

    sealed class Result {
        data class Success(val listing: Listing): Result()
        data class Failure(val throwable: Throwable): Result()
    }

    suspend fun getArticleListings(pageCount: Int): Result {

//        return try {
            val result = apiServices.getArticleListings(pageCount)
            return Result.Success(result)
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
    }
}