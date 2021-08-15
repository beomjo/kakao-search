package io.github.beomjo.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.beomjo.search.entity.Document

class DocumentDiffUtil : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(
        oldItem: Document,
        newItem: Document
    ): Boolean =
        oldItem.title == newItem.title &&
                oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: Document,
        newItem: Document
    ): Boolean =
        oldItem == newItem
}
