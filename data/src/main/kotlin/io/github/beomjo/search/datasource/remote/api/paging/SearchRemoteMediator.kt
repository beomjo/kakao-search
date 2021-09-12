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

package io.github.beomjo.search.datasource.remote.api.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.datasource.local.AppDatabase
import io.github.beomjo.search.datasource.local.table.SearchDocumentTable
import io.github.beomjo.search.datasource.local.table.RemoteKeyTable
import io.github.beomjo.search.datasource.remote.api.service.DocumentsApi
import io.github.beomjo.search.entity.SearchDocumentList
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.mapper.toEntity
import io.github.beomjo.search.mapper.toDocumentTable
import io.github.beomjo.search.usecase.SearchPagingParam
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class SearchRemoteMediator @AssistedInject constructor(
    @Assisted private val requestParam: SearchPagingParam,
    private val documentApi: DocumentsApi,
    private val database: AppDatabase,
) : RemoteMediator<Int, SearchDocumentTable>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchDocumentTable>
    ): MediatorResult {
        try {
            val position: Int = when (loadType) {
                LoadType.REFRESH -> STARTING_POSITION
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val key = database.remoteKeyDao().getRemoteKeyTable()?.lastOrNull()
                    val nextKey = key?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = key != null)
                    nextKey
                }
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.documentDao().clearAllDocuments()
                    database.remoteKeyDao().clearRemoteKeys()
                }
            }

            val documentList = fetchDocumentList(position)

            database.withTransaction {
                val prevKey = if (position == STARTING_POSITION) null else position - 1
                val nextKey = if (documentList.hasMore) position + 1 else null
                val keys = RemoteKeyTable(position = position, prevKey = prevKey, nextKey = nextKey)

                database.documentDao().insertDocuments(documentList.searchDocuments.map { it.toDocumentTable() })
                database.remoteKeyDao().insertKey(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = !documentList.hasMore)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun fetchDocumentList(position: Int) = when (requestParam.documentType) {
        DocumentType.ALL -> {
            val pageSize = PER_PAGE_SIZE / DOCUMENT_TYPE_NUM
            val blogDocumentList = fetchBlogList(position, pageSize)
            val cafeDocumentList = fetchCafeList(position, pageSize)
            val webDocumentList = fetchWebList(position, pageSize)
            val imageDocumentList = fetchImageList(position, pageSize)
            val bookDocumentList = fetchBookList(position, pageSize)

            blogDocumentList + cafeDocumentList + webDocumentList + imageDocumentList + bookDocumentList
        }
        DocumentType.BLOG -> fetchBlogList(position, PER_PAGE_SIZE)
        DocumentType.CAFE -> fetchCafeList(position, PER_PAGE_SIZE)
        DocumentType.WEB -> fetchWebList(position, PER_PAGE_SIZE)
        DocumentType.IMAGE -> fetchImageList(position, PER_PAGE_SIZE)
        DocumentType.BOOK -> fetchBookList(position, PER_PAGE_SIZE)
    }

    private suspend fun fetchBlogList(pagePosition: Int, pageSize: Int): SearchDocumentList {
        if (pagePosition > MAX_PAGE_POSITION) return getEmptyDocumentList()
        return documentApi.fetchBlog(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()
    }

    private suspend fun fetchCafeList(pagePosition: Int, pageSize: Int): SearchDocumentList {
        if (pagePosition > MAX_PAGE_POSITION) return getEmptyDocumentList()
        return documentApi.fetchCafe(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()
    }

    private suspend fun fetchWebList(pagePosition: Int, pageSize: Int): SearchDocumentList {
        if (pagePosition > MAX_PAGE_POSITION) return getEmptyDocumentList()
        return documentApi.fetchWeb(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()
    }

    private suspend fun fetchImageList(pagePosition: Int, pageSize: Int): SearchDocumentList {
        if (pagePosition > MAX_PAGE_POSITION) return getEmptyDocumentList()
        val documentList = documentApi.fetchImages(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()
        return documentList.copy(
            searchDocuments = documentList.searchDocuments.map {
                it.copy(content = requestParam.query)
            }
        )
    }

    private suspend fun fetchBookList(pagePosition: Int, pageSize: Int): SearchDocumentList {
        if (pagePosition > MAX_PAGE_POSITION) return getEmptyDocumentList()
        return documentApi.fetchBook(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()
    }

    private fun getEmptyDocumentList() = SearchDocumentList(hasMore = false, emptyList())

    companion object {
        const val STARTING_POSITION = 1
        const val MAX_PAGE_POSITION = 50
        const val PER_PAGE_SIZE = 25
        val DOCUMENT_TYPE_NUM = DocumentType.values().size - 1
    }
}

@AssistedFactory
internal interface SearchRemoteMediatorFactory {
    fun create(requestParam: SearchPagingParam): SearchRemoteMediator
}
