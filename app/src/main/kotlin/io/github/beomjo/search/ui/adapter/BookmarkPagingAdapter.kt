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
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.R
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.ui.adapter.diff.SearchDocumentDiffUtil
import io.github.beomjo.search.ui.adapter.diff.SearchUiItemDiffUtil
import io.github.beomjo.search.ui.adapter.viewholders.BookmarkViewHolder
import io.github.beomjo.search.ui.adapter.viewholders.SearchDocumentViewHolder
import io.github.beomjo.search.ui.adapter.viewholders.SearchSeparatorViewHolder
import io.github.beomjo.search.ui.viewmodels.SearchDocumentViewModelFactory
import io.github.beomjo.search.ui.viewmodels.SearchViewModel
import javax.inject.Provider

class BookmarkPagingAdapter @AssistedInject constructor(
    @Assisted
    private val lifeCycleOwner: LifecycleOwner,
    @Assisted
    private val onClickItem: (SearchDocument) -> Unit,
    private val factory: Provider<SearchDocumentViewModelFactory>
) : PagingDataAdapter<SearchDocument, BookmarkViewHolder>(SearchDocumentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(
                lifeCycleOwner,
                factory.get().create(it),
                onClickItem
            )
        }
    }
}

@AssistedFactory
interface BookmarkPagingAdapterFactory {
    fun create(
        lifeCycleOwner: LifecycleOwner,
        onClickItem: (SearchDocument) -> Unit
    ): BookmarkPagingAdapter
}