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
import io.github.beomjo.search.model.ModelVideo
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
