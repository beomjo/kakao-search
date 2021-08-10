package io.github.beomjo.search.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.R
import io.github.beomjo.search.databinding.DocumentListFooterItemBinding

class DocumentLoadStateViewHolder(
    private val binding: DocumentListFooterItemBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            retryBtn.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
            retryBtn.setOnClickListener {
                retry.invoke()
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): DocumentLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.document_list_footer_item, parent, false)
            val binding = DocumentListFooterItemBinding.bind(view)
            return DocumentLoadStateViewHolder(binding, retry)
        }
    }
}