package com.example.dysnatyinterview.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dysnatyinterview.model.Article
import com.example.dysnatyinterview.repository.ListingRepository
import com.example.dysnatyinterview.services.APIClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListingViewModel: ViewModel() {
    private val apiService = APIClient.service
    private var listingRepository = ListingRepository(apiService)
    private var pageCount = 1
    private var hasNextPage = true

    var mArticles: List<Article> by mutableStateOf(listOf())

    lateinit var clickedItem: Article

    init {
        getListings()
//        getListings()
    }

    fun getListings() {
        if (!hasNextPage) return
        viewModelScope.launch(Dispatchers.IO) {
            var response = listingRepository.getArticleListings(pageCount)
            when (response) {
                is ListingRepository.Result.Success -> {
                    val articlesJsonArray = response.listing.articles
                    val articles = mutableListOf<Article>()
                    for (i in 0 until articlesJsonArray.count()) {
                        val articleJsonObject = articlesJsonArray.get(i).asJsonObject

                        val article = try {
                            Gson().fromJson(articleJsonObject, Article::class.java)
                        } catch (e: java.lang.Exception) {
                            null
                        }

                        if (article != null && article.articleStatus.equals("publish", true)) {
                            articles.add(article)
                        }
                    }

                    if (response.listing.hasNextPage == true) {
                        pageCount += 1
                    } else {
                        hasNextPage = false
                    }
                    Log.d("MAINTAG", "getListings: " + mArticles.count())
                    mArticles = mArticles + articles
                }
                is ListingRepository.Result.Failure -> {
                    Log.d("MAINTAG", "getListings: " + response.throwable.message)
                }
            }
        }
    }

    fun itemClicked(article: Article) {
        clickedItem = article
    }
}