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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.datasource.remote.api.service.DocumentsApi
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.mapper.toEntity
import io.github.beomjo.search.usecase.SearchPagingParam
import retrofit2.HttpException
import java.io.IOException

internal class SearchPagingSource @AssistedInject constructor(
    @Assisted private val requestParam: SearchPagingParam,
    private val documentApi: DocumentsApi,
) : PagingSource<SearchPagingSource.PagingParam, Document>() {

    override suspend fun load(params: LoadParams<PagingParam>): LoadResult<PagingParam, Document> {
        val param = params.key ?: getDefaultPagingParam(requestParam.documentType)
        val pagePosition = param.getPagePosition()
        return try {
            val documentList = when (requestParam.documentType) {
                DocumentType.ALL -> {
                    val blogDocumentList = fetchBlogList(pagePosition, 5)
                    val cafeDocumentList = fetchCafeList(pagePosition, 5)
                    val webDocumentList = fetchWebList(pagePosition, 5)
                    val imageDocumentList = fetchImageList(pagePosition, 5)
                    val bookDocumentList = fetchBookList(pagePosition, 5)

                    blogDocumentList + cafeDocumentList + webDocumentList + imageDocumentList + bookDocumentList
                }
                DocumentType.BLOG -> fetchBlogList(pagePosition, 25)
                DocumentType.CAFE -> fetchCafeList(pagePosition, 25)
                DocumentType.WEB -> fetchWebList(pagePosition, 25)
                DocumentType.IMAGE -> fetchImageList(pagePosition, 25)
                DocumentType.BOOK -> fetchBookList(pagePosition, 25)
            }

            return LoadResult.Page(
                when (requestParam.sortType) {
                    SortType.TITLE -> documentList.documents.sortedBy { it.title }
                    else -> documentList.documents.sortedByDescending { it.date }
                },
                prevKey = param.prevPage(),
                nextKey = param.nextPage(requestParam.documentType)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<PagingParam, Document>): PagingParam? {
        return state.anchorPosition
            ?.let { state.closestPageToPosition(it) }
            ?.let { it.prevKey ?: it.nextKey }
    }

    private suspend fun fetchBlogList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchBlog(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()

    private suspend fun fetchCafeList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchCafe(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()


    private suspend fun fetchWebList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchWeb(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()

    private suspend fun fetchImageList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchImages(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()

    private suspend fun fetchBookList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchBook(
            query = requestParam.query,
            sort = requestParam.sort.value,
            page = pagePosition,
            size = pageSize
        ).toEntity()

    private fun getDefaultPagingParam(documentType: DocumentType) = when (documentType) {
        DocumentType.ALL -> PagingParam(
            PagingParam.Key(position = STARTING_POSITION, hasMore = true),
            PagingParam.Key(position = STARTING_POSITION, hasMore = true),
            PagingParam.Key(position = STARTING_POSITION, hasMore = true),
            PagingParam.Key(position = STARTING_POSITION, hasMore = true),
            PagingParam.Key(position = STARTING_POSITION, hasMore = true)
        )
        DocumentType.BLOG -> PagingParam(
            blogKey = PagingParam.Key(
                position = STARTING_POSITION,
                hasMore = true
            )
        )
        DocumentType.CAFE -> PagingParam(
            cafeKey = PagingParam.Key(
                position = STARTING_POSITION,
                hasMore = true
            )
        )
        DocumentType.WEB -> PagingParam(
            webKey = PagingParam.Key(
                position = STARTING_POSITION,
                hasMore = true
            )
        )
        DocumentType.IMAGE -> PagingParam(
            imageKey = PagingParam.Key(
                position = STARTING_POSITION,
                hasMore = true
            )
        )
        DocumentType.BOOK -> PagingParam(
            bookKey = PagingParam.Key(
                position = STARTING_POSITION,
                hasMore = true
            )
        )
    }

    data class PagingParam(
        val blogKey: Key = Key(),
        val cafeKey: Key = Key(),
        val webKey: Key = Key(),
        val imageKey: Key = Key(),
        val bookKey: Key = Key(),
    ) {

        data class Key(
            val position: Int = 0,
            val hasMore: Boolean = false
        ) {
            fun next(): Key = copy(position = position + 1)

            fun prev(): Key = copy(position = position - 1, hasMore = true)
        }

        fun getPagePosition(): Int = maxOf(
            blogKey.position,
            cafeKey.position,
            webKey.position,
            imageKey.position,
            bookKey.position
        )

        fun nextPage(documentType: DocumentType): PagingParam = when (documentType) {
            DocumentType.ALL -> copy(
                blogKey = if (blogKey.hasMore) blogKey.next() else blogKey,
                cafeKey = if (cafeKey.hasMore) cafeKey.next() else cafeKey,
                webKey = if (webKey.hasMore) webKey.next() else webKey,
                imageKey = if (imageKey.hasMore) imageKey.next() else imageKey,
                bookKey = if (bookKey.hasMore) bookKey.next() else bookKey,
            )
            DocumentType.BLOG -> copy(blogKey = if (blogKey.hasMore) blogKey.next() else blogKey)
            DocumentType.CAFE -> copy(cafeKey = if (cafeKey.hasMore) cafeKey.next() else cafeKey)
            DocumentType.WEB -> copy(webKey = if (webKey.hasMore) webKey.next() else webKey)
            DocumentType.IMAGE -> copy(imageKey = if (imageKey.hasMore) imageKey.next() else imageKey)
            DocumentType.BOOK -> copy(bookKey = if (bookKey.hasMore) bookKey.next() else bookKey)
        }

        fun prevPage(): PagingParam? {
            val position = getPagePosition()
            if (position == STARTING_POSITION) return null
            return PagingParam(
                blogKey = if (blogKey.position != position) blogKey else blogKey.prev(),
                cafeKey = if (cafeKey.position != position) cafeKey else cafeKey.prev(),
                webKey = if (webKey.position != position) webKey else webKey.prev(),
                imageKey = if (imageKey.position != position) imageKey else imageKey.prev(),
                bookKey = if (bookKey.position != position) bookKey else bookKey.prev(),
            )
        }
    }

    companion object {
        const val STARTING_POSITION = 1
        const val PER_PAGE_SIZE = 25
    }
}

@AssistedFactory
internal interface SearchPagingSourceFactory {
    fun create(requestParam: SearchPagingParam): SearchPagingSource
}


