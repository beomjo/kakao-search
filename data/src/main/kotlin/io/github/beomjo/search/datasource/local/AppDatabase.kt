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

package io.github.beomjo.search.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.beomjo.search.datasource.local.converter.DateConverter
import io.github.beomjo.search.datasource.local.dao.*
import io.github.beomjo.search.datasource.local.dao.BookmarkDao
import io.github.beomjo.search.datasource.local.dao.RemoteKeyDao
import io.github.beomjo.search.datasource.local.dao.SearchDocumentDao
import io.github.beomjo.search.datasource.local.dao.SearchDocumentVisitDao
import io.github.beomjo.search.datasource.local.dao.SearchHistoryDao
import io.github.beomjo.search.datasource.local.table.*
import io.github.beomjo.search.datasource.local.table.BookmarkTable
import io.github.beomjo.search.datasource.local.table.SearchDocumentTable
import io.github.beomjo.search.datasource.local.table.SearchHistoryTable
import io.github.beomjo.search.datasource.local.table.VisitTable

@Database(
    entities = [
        SearchDocumentTable::class,
        RemoteKeyTable::class,
        SearchHistoryTable::class,
        VisitTable::class,
        BookmarkTable::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun documentDao(): SearchDocumentDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun searchDocumentVisitDao(): SearchDocumentVisitDao

    abstract fun bookmarkDao(): BookmarkDao

    companion object {

        private const val NAME = "app_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    NAME
                ).build()
            }
        }
    }
}
