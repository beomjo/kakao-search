package io.github.beomjo.search.datasource.remote.api.service

import io.github.beomjo.search.model.*
import retrofit2.http.GET

internal interface DocumentsApi {
    @GET("search/blog")
    suspend fun fetchBlog(): ModelDocumentList<ModelBlog>

    @GET("search/cafe")
    suspend fun fetchCafe(): ModelDocumentList<ModelCafe>

    @GET("search/image")
    suspend fun fetchImages(): ModelDocumentList<ModelImage>

    @GET("search/vclip")
    suspend fun fetchVideo(): ModelDocumentList<ModelVideo>

    @GET("search/book")
    suspend fun fetchBook(): ModelDocumentList<ModelBook>
}