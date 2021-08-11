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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.beomjo.search.datasource.remote.api.paging.SearchPagingSource
import io.github.beomjo.search.datasource.remote.api.paging.SearchPagingSourceFactory
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.usecase.SearchPagingParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DocumentRepositoryImpl @Inject constructor(
    private val searchPagingDataSourceFactory: SearchPagingSourceFactory
) : DocumentRepository {
    override fun fetchDocumentPagingData(param: SearchPagingParam): Flow<PagingData<Document>> {
        return Pager(
            config = PagingConfig(
                pageSize = SearchPagingSource.PER_PAGE_SIZE,
            ),
            pagingSourceFactory = { searchPagingDataSourceFactory.create(param) }
        ).flow
    }
}
