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
import io.github.beomjo.search.datasource.local.dao.BookmarkDao
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.mapper.toEntity
import javax.inject.Inject

internal class BookmarkPagingSource @Inject constructor(
    private val bookmarkDao: BookmarkDao,
) : PagingSource<Int, SearchDocument>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchDocument> {
        return try {
            val offset = params.key ?: DEFAULT_OFFSET
            val bookmarkList = bookmarkDao.getBookmarks(offset = offset, limit = LIMIT)
            LoadResult.Page(
                data = bookmarkList.map { it.toEntity() },
                prevKey = null,
                nextKey = if (bookmarkList.isEmpty() || bookmarkList.size < LIMIT) null else offset + LIMIT
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchDocument>): Int? {
        return null
    }

    companion object {
        const val DEFAULT_OFFSET = 0
        const val LIMIT = 10
    }
}