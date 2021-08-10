/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.beomjo.search.datasource.remote.api.service

import io.github.beomjo.search.model.ModelBlog
import io.github.beomjo.search.model.ModelBook
import io.github.beomjo.search.model.ModelCafe
import io.github.beomjo.search.model.ModelDocumentList
import io.github.beomjo.search.model.ModelImage
import io.github.beomjo.search.model.ModelWeb
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DocumentsApi {
    @GET("v2/search/blog")
    suspend fun fetchBlog(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int?,
        @Query(SIZE) size: Int?,
    ): ModelDocumentList<ModelBlog>

    @GET("v2/search/cafe")
    suspend fun fetchCafe(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int?,
        @Query(SIZE) size: Int?,
    ): ModelDocumentList<ModelCafe>

    @GET("v2/search/image")
    suspend fun fetchImages(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int?,
        @Query(SIZE) size: Int?,
    ): ModelDocumentList<ModelImage>

    @GET("v2/search/web")
    suspend fun fetchWeb(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int?,
        @Query(SIZE) size: Int?,
    ): ModelDocumentList<ModelWeb>

    @GET("v3/search/book")
    suspend fun fetchBook(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int?,
        @Query(SIZE) size: Int?,
    ): ModelDocumentList<ModelBook>

    companion object {
        const val QUERY = "query"
        const val SORT = "sort"
        const val PAGE = "page"
        const val SIZE = "size"
    }
}
