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
                    oldItem.searchDocument.title == newItem.searchDocument.title &&
                    oldItem.searchDocument.date == newItem.searchDocument.date
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
