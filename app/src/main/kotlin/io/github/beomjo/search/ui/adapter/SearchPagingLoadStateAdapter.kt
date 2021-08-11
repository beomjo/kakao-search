package io.github.beomjo.search.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import io.github.beomjo.search.ui.adapter.viewholders.DocumentLoadStateViewHolder

class SearchPagingLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<DocumentLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: DocumentLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): DocumentLoadStateViewHolder {
        return DocumentLoadStateViewHolder.create(parent, retry)
    }
}
