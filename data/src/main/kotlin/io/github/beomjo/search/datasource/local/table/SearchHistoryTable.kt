package io.github.beomjo.search.datasource.local.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "search_history")
internal data class SearchHistoryTable(
    @PrimaryKey
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("date")
    val date: Date,
)