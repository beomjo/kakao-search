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

package io.github.beomjo.search.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.databinding.BookmarkListItemBinding
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.ui.viewmodels.SearchDocumentViewModel

class BookmarkViewHolder(
    private val binding: BookmarkListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        lifecycleOwner: LifecycleOwner,
        searchDocumentViewModel: SearchDocumentViewModel,
        onClickItem: (SearchDocument) -> Unit,
    ) {
        binding.run {
            this.lifecycleOwner = lifecycleOwner
            viewModel = searchDocumentViewModel
            documentContainer.setOnClickListener { onClickItem(searchDocumentViewModel.searchDocument) }
            bookmarkBtn.setOnClickListener {
                searchDocumentViewModel.onClickBookmark()
            }
            executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup): BookmarkViewHolder {
            return BookmarkViewHolder(
                BookmarkListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
