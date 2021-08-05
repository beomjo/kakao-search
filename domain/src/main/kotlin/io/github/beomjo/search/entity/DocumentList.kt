package io.github.beomjo.search.entity

data class DocumentList(
    val hasMore: Boolean,
    val documents: List<Document>
)