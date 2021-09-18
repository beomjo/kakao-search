package io.github.beomjo.search.repository

import androidx.paging.PagingData
import io.github.beomjo.search.entity.SearchDocument
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun setBookmark(searchDocument: SearchDocument)

    suspend fun removeBookmark(searchDocument: SearchDocument)

    fun isBookmarked(searchDocument: SearchDocument): Flow<Boolean>

    fun getBookmarkPagingData(): Flow<PagingData<SearchDocument>>

    fun getBookmarkList(): Flow<List<SearchDocument>>
}