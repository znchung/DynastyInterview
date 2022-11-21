package com.example.dysnatyinterview.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Listing(
    val hasNextPage: Boolean?,
    val articles: JsonArray
)

data class Article(
    val id: String?,
    val author: Author?,
    val game: String?,
    val image: String?,
    val articleStatus: String?,
    val title: Title?,
    val gameDetails: String?,
    val createdOn: String?,
    val content: Content?,
    val category: Category?
)

data class Author(
    val id: String?,
    val username: String?,
    val profilePicture: String?
)

data class Title(
    val english: String?,
    val arabic: String?
)

data class Category(
    @SerializedName("_id")
    val id: String?,
    val name: String?
)

data class Content(
    val english: String?,
    val arabic: String?
)
