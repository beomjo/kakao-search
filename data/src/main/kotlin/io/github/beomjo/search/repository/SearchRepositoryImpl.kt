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

import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.github.beomjo.search.datasource.local.dao.SearchDocumentDao
import io.github.beomjo.search.datasource.local.dao.SearchHistoryDao
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediator
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediatorFactory
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.entity.History
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.mapper.toEntity
import io.github.beomjo.search.mapper.toTable
import io.github.beomjo.search.usecase.SearchPagingParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteMediatorFactory: SearchRemoteMediatorFactory,
    private val documentDao: SearchDocumentDao,
    private val searchHistoryDao: SearchHistoryDao
) : SearchRepository {
    override fun getDocumentPagingData(param: SearchPagingParam): Flow<PagingData<SearchDocument>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = SearchRemoteMediator.PER_PAGE_SIZE,
                prefetchDistance = 3

            ),
            remoteMediator = searchRemoteMediatorFactory.create(param)
        ) {
            when (param.sortType) {
                SortType.TITLE -> documentDao.getDocumentByTitle(param.query)
                else -> documentDao.getDocumentByDate(param.query)
            }
        }.flow.map {
            it.map { documentTable ->
                documentTable.toEntity()
            }
        }
    }

    override suspend fun insertSearchHistory(history: History) {
        return searchHistoryDao.insertHistory(history.toTable())
    }

    override fun getSearchHistoryList(): Flow<List<History>> {
        return searchHistoryDao.getHistoryList().map { historyList ->
            historyList.map { it.toEntity() }
        }
    }

    override suspend fun setVisit(visit: Visit) {
    }

    override fun getVisit(url: String): Flow<Visit> {
        return flowOf()
    }
}
