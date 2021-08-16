/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.beomjo.search.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.R
import io.github.beomjo.search.ui.adapter.diff.DocumentDiffUtil
import io.github.beomjo.search.ui.adapter.viewholders.DocumentViewHolder
import io.github.beomjo.search.ui.adapter.viewholders.SeparatorViewHolder
import io.github.beomjo.search.ui.viewmodels.SearchViewModel.SearchUiItem

class SearchPagingAdapter :
    PagingDataAdapter<SearchUiItem, RecyclerView.ViewHolder>(DocumentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.document_list_item -> DocumentViewHolder.create(parent)
            else -> SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchUiItem.DocumentItem -> R.layout.document_list_item
            is SearchUiItem.SeparatorItem -> R.layout.separator_list_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is SearchUiItem.DocumentItem -> (holder as DocumentViewHolder).bind(it.document)
                is SearchUiItem.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.description)
            }
        }
    }

}
