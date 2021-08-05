package io.github.beomjo.search.entity

data class Document(
    val type: DocumentType,
    val url: String,
    val thumbnail: String,
    val title: String,
    val content: String,
    val date: String,
)