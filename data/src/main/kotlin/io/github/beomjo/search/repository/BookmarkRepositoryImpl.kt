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
import io.github.beomjo.search.datasource.local.dao.BookmarkDao
import io.github.beomjo.search.datasource.remote.api.paging.BookmarkPagingSource
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.mapper.toBookmarkTable
import io.github.beomjo.search.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val bookmarkPagingSource: BookmarkPagingSource
) : BookmarkRepository {
    override suspend fun setBookmark(searchDocument: SearchDocument) {
        bookmarkDao.insertBookmark(searchDocument.toBookmarkTable())
    }

    override suspend fun removeBookmark(searchDocument: SearchDocument) {
        bookmarkDao.deleteBookmark(searchDocument.url)
    }

    override fun isBookmarked(searchDocument: SearchDocument): Flow<Boolean> {
        return bookmarkDao.isBookmarked(searchDocument.url).map { it?.url != null }
    }

    override fun getBookmarkPagingData(): Flow<PagingData<SearchDocument>> {
        return Pager(
            config = PagingConfig(
                pageSize = BookmarkPagingSource.LIMIT,
                enablePlaceholders = false
            )
        ) { bookmarkPagingSource }.flow
    }

    override fun getBookmarkList(): Flow<List<SearchDocument>> {
        return bookmarkDao.getBookmarks().map { bookmarkTable ->
            bookmarkTable.map { it.toEntity() }
        }
    }
}