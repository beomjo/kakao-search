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
