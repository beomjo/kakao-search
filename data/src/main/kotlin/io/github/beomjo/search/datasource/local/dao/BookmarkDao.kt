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

package io.github.beomjo.search.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.beomjo.search.datasource.local.table.BookmarkTable
import io.github.beomjo.search.datasource.local.table.SearchDocumentTable
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkTable)

    @Query("DELETE FROM bookmark_table WHERE url == :url")
    fun deleteBookmark(url: String)

    @Query("SELECT * FROM bookmark_table WHERE url == :url")
    fun isBookmarked(url: String) : Flow<BookmarkTable?>

//    @Query("SELECT * FROM bookmark_table")
//    fun getDocumentByDate(query: String): PagingSource<Int, SearchDocumentTable>
}
