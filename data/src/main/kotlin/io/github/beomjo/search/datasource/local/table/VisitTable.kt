package io.github.beomjo.search.datasource.local.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "visit")
internal data class VisitTable(
    @PrimaryKey
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("date")
    val date: Date?,
)
