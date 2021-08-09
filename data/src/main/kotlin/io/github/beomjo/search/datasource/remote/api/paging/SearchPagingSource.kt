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
        val param = params.key
        val pagePosition = param?.getPagePosition() ?: STARTING_POSITION
        return try {
            val documentList = when (requestParam.documentType) {
                DocumentType.ALL -> {
                    val blogDocumentList = fetchBlogList(pagePosition, 5)
                    val cafeDocumentList = fetchCafeList(pagePosition, 5)
                    val videoDocumentList = fetchVideoList(pagePosition, 5)
                    val imageDocumentList = fetchImageList(pagePosition, 5)
                    val bookDocumentList = fetchBookList(pagePosition, 5)

                    blogDocumentList + cafeDocumentList + videoDocumentList + imageDocumentList + bookDocumentList
                }
                DocumentType.BLOG -> fetchBlogList(pagePosition, 25)
                DocumentType.CAFE -> fetchCafeList(pagePosition, 25)
                DocumentType.VIDEO -> fetchVideoList(pagePosition, 25)
                DocumentType.IMAGE -> fetchImageList(pagePosition, 25)
                DocumentType.BOOK -> fetchBookList(pagePosition, 25)
            }

            return LoadResult.Page(
                when (requestParam.sortType) {
                    SortType.TITLE -> documentList.documents.sortedBy { it.title }
                    else -> documentList.documents.sortedByDescending { it.date }
                },
                prevKey = param?.prevPage(),
                nextKey = param?.nextPage(requestParam.documentType)
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


    private suspend fun fetchVideoList(pagePosition: Int, pageSize: Int) =
        documentApi.fetchVideo(
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


    data class PagingParam(
        val blogKey: Key = Key(),
        val cafeKey: Key = Key(),
        val videoKey: Key = Key(),
        val imageKey: Key = Key(),
        val bookKey: Key = Key(),
    ) {

        data class Key(
            val position: Int = 1,
            val hasMore: Boolean = false
        ) {
            fun next(): Key = copy(position = position + 1)

            fun prev(): Key = copy(position = position - 1, hasMore = true)
        }

        fun getPagePosition(): Int = maxOf(
            blogKey.position,
            cafeKey.position,
            videoKey.position,
            imageKey.position,
            bookKey.position
        )

        fun nextPage(documentType: DocumentType): PagingParam = when (documentType) {
            DocumentType.ALL -> copy(
                blogKey = if (blogKey.hasMore) blogKey.next() else blogKey,
                cafeKey = if (cafeKey.hasMore) cafeKey.next() else cafeKey,
                videoKey = if (videoKey.hasMore) videoKey.next() else videoKey,
                imageKey = if (imageKey.hasMore) imageKey.next() else imageKey,
                bookKey = if (bookKey.hasMore) bookKey.next() else bookKey,
            )
            DocumentType.BLOG -> copy(blogKey = if (blogKey.hasMore) blogKey.next() else blogKey)
            DocumentType.CAFE -> copy(cafeKey = if (cafeKey.hasMore) cafeKey.next() else cafeKey)
            DocumentType.VIDEO -> copy(videoKey = if (videoKey.hasMore) videoKey.next() else videoKey)
            DocumentType.IMAGE -> copy(imageKey = if (imageKey.hasMore) imageKey.next() else imageKey)
            DocumentType.BOOK -> copy(bookKey = if (bookKey.hasMore) bookKey.next() else bookKey)
        }

        fun prevPage(): PagingParam? {
            if (getPagePosition() == STARTING_POSITION) return null
            return PagingParam(
                blogKey = if (blogKey.hasMore) blogKey.prev() else blogKey,
                cafeKey = if (cafeKey.hasMore) cafeKey.prev() else cafeKey,
                videoKey = if (videoKey.hasMore) videoKey.prev() else videoKey,
                imageKey = if (imageKey.hasMore) imageKey.prev() else imageKey,
                bookKey = if (bookKey.hasMore) bookKey.prev() else bookKey,
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


