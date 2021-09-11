package io.github.beomjo.search.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.beomjo.search.datasource.local.table.VisitTable
import io.github.beomjo.search.entity.Visit
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SearchDocumentVisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitTable)

    @Query("SELECT * FROM visit WHERE url == :url")
    fun getVisit(url: String): Flow<VisitTable?>
}