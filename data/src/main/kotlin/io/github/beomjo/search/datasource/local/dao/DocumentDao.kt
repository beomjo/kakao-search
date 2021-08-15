package io.github.beomjo.search.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.beomjo.search.datasource.local.table.DocumentTable

@Dao
internal interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<DocumentTable>)

    @Query("SELECT * FROM document_table WHERE url = :url ORDER BY date DESC")
    suspend fun getDocumentByDate(url: String): DocumentTable

    @Query("SELECT * FROM document_table WHERE url = :url ORDER BY title ASC")
    suspend fun getDocumentByTitle(url: String): DocumentTable

    @Query("DELETE from document_table")
    suspend fun clearAllDocuments()
}