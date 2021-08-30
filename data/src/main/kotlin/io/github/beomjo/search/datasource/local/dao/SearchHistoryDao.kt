package io.github.beomjo.search.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.beomjo.search.datasource.local.table.SearchHistoryTable
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: SearchHistoryTable)

    @Query("SELECT * FROM search_history")
    fun getHistoryList(): Flow<List<SearchHistoryTable>>
}
