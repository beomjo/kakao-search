package io.github.beomjo.search.model

import com.google.gson.annotations.SerializedName


internal data class ModelVideo(
    @SerializedName("author")
    val author: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("play_time")
    val play_time: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)