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

package io.github.beomjo.search.repository

import io.github.beomjo.search.datasource.remote.api.service.DocumentsApi
import io.github.beomjo.search.entity.DocumentList
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.mapper.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DocumentRepositoryImpl @Inject constructor(
    private val documentsApi: DocumentsApi
) : DocumentRepository {

    override suspend fun fetchBlog(
        query: String,
        sort: Sort,
        page: Int?,
        size: Int?,
    ): DocumentList {
        return documentsApi.fetchBlog(
            query = query,
            sort = sort.value,
            page = page,
            size = size,
        ).toEntity()
    }

    override suspend fun fetchCafe(
        query: String,
        sort: Sort,
        page: Int?,
        size: Int?,
    ): DocumentList {
        return documentsApi.fetchCafe(
            query = query,
            sort = sort.value,
            page = page,
            size = size,
        ).toEntity()
    }

    override suspend fun fetchImages(
        query: String,
        sort: Sort,
        page: Int?,
        size: Int?,
    ): DocumentList {
        return documentsApi.fetchImages(
            query = query,
            sort = sort.value,
            page = page,
            size = size,
        ).toEntity()
    }

    override suspend fun fetchVideo(
        query: String,
        sort: Sort,
        page: Int?,
        size: Int?,
    ): DocumentList {
        return documentsApi.fetchVideo(
            query = query,
            sort = sort.value,
            page = page,
            size = size,
        ).toEntity()
    }

    override suspend fun fetchBook(
        query: String,
        sort: Sort,
        page: Int?,
        size: Int?,
    ): DocumentList {
        return documentsApi.fetchBook(
            query = query,
            sort = sort.value,
            page = page,
            size = size,
        ).toEntity()
    }
}
