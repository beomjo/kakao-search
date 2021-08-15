package io.github.beomjo.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.ui.adapter.viewholders.SearchControlMenuViewHolder

class SearchControlMenuAdapter(
    private val onFilterSelected: (DocumentType) -> Unit,
    private val onSortSelected: (SortType) -> Unit,
) : RecyclerView.Adapter<SearchControlMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchControlMenuViewHolder {
        return SearchControlMenuViewHolder.create(parent).apply {
            bind(onFilterSelected, onSortSelected)
        }
    }

    override fun onBindViewHolder(holder: SearchControlMenuViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 1
}
