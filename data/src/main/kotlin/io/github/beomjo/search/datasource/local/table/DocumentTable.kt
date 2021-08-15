package io.github.beomjo.search.datasource.local.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.github.beomjo.search.entity.DocumentType
import java.util.Date

@Entity(tableName = "document_table")
internal data class DocumentTable(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("type")
    val type: DocumentType,
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("thumbnail")
    val thumbnail: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("content")
    val content: String,
    @field:SerializedName("date")
    val date: Date,
)
