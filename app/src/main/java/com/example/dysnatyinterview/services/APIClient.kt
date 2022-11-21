package com.example.dysnatyinterview.services

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


object APIClient {
    val interceptor = HttpLoggingInterceptor()
    var httpClient = OkHttpClient.Builder()

    const val BASE_URL = "https://api.mocki.io/"


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build())
        .addConverterFactory(GsonConverterFactory.create(getGson()))

    val service: APIServices by lazy {
        retrofit.build().create(APIServices::class.java)
    }

    fun getGson(): Gson {
        val gsonBuilder = GsonBuilder().serializeNulls()
//        gsonBuilder.registerTypeHierarchyAdapter(
//            Date::class.java,
//            GsonDateAdapter()
//        )

//        val strategy = object : ExclusionStrategy {
//            override fun shouldSkipClass(clazz: Class<*>): Boolean {
//                return false
//            }
//
//            override fun shouldSkipField(field: FieldAttributes): Boolean {
//                return field.getAnnotation(Exclude::class.java) != null
//            }
//        }
        return gsonBuilder.create() //.addSerializationExclusionStrategy(strategy).create()
    }
}