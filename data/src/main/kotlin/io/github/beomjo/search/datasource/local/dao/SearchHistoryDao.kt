package io.github.beomjo.search.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.beomjo.search.datasource.local.table.SearchHistoryTable

@Dao
internal interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: SearchHistoryTable)

    @Query("SELECT * FROM search_history")
    suspend fun getHistoryList(): List<SearchHistoryTable>?

}