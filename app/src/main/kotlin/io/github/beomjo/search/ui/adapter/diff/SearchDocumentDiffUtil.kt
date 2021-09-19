package io.github.beomjo.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.beomjo.search.entity.SearchDocument

class SearchDocumentDiffUtil : DiffUtil.ItemCallback<SearchDocument>() {
    override fun areItemsTheSame(
        oldItem: SearchDocument,
        newItem: SearchDocument
    ): Boolean {
        val isSameDocumentItem = oldItem.title == newItem.title && oldItem.date == newItem.date
        val isSameSeparatorItem = oldItem == newItem
        return isSameDocumentItem || isSameSeparatorItem
    }

    override fun areContentsTheSame(
        oldItem: SearchDocument,
        newItem: SearchDocument
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
