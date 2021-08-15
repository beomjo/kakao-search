package io.github.beomjo.search.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.databinding.DocumentListItemBinding
import io.github.beomjo.search.entity.Document

class DocumentViewHolder(
    private val binding: DocumentListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(document: Document) {
        binding.document = document
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): DocumentViewHolder {
            return DocumentViewHolder(
                DocumentListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
