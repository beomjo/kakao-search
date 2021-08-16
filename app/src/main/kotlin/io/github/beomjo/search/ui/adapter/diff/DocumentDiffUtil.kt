package io.github.beomjo.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.beomjo.search.ui.viewmodels.SearchViewModel.SearchUiItem

class DocumentDiffUtil : DiffUtil.ItemCallback<SearchUiItem>() {
    override fun areItemsTheSame(
        oldItem: SearchUiItem,
        newItem: SearchUiItem
    ): Boolean {
        val isSameDocumentItem =
            oldItem is SearchUiItem.DocumentItem && newItem is SearchUiItem.DocumentItem &&
                    oldItem.document.title == newItem.document.title &&
                    oldItem.document.date == newItem.document.date
        val isSameSeparatorItem =
            oldItem is SearchUiItem.SeparatorItem && newItem is SearchUiItem.SeparatorItem &&
                    oldItem.description == newItem.description
        return isSameDocumentItem || isSameSeparatorItem
    }

    override fun areContentsTheSame(
        oldItem: SearchUiItem,
        newItem: SearchUiItem
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
