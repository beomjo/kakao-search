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

package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.GetBookmark
import io.github.beomjo.search.usecase.GetSearchDocumentVisit
import io.github.beomjo.search.usecase.RemoveBookmark
import io.github.beomjo.search.usecase.SetBookmark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchDocumentViewModel @AssistedInject constructor(
    @Assisted val searchDocument: SearchDocument,
    getSearchDocumentVisit: GetSearchDocumentVisit,
    getBookmark: GetBookmark,
    private val setBookmark: SetBookmark,
    private val removeBookmark: RemoveBookmark
) {
    val isVisit: LiveData<Boolean> = getSearchDocumentVisit(searchDocument.url)
        .distinctUntilChanged()
        .flowOn(Dispatchers.Main)
        .asLiveData()
        .map { it?.url != null }

    val isBookmarked: LiveData<Boolean> = getBookmark(searchDocument)
        .flowOn(Dispatchers.Main)
        .asLiveData()

    fun onClickBookmark() {
        CoroutineScope(Dispatchers.Main).launch {
            if (isBookmarked.value == false) {
                setBookmark(searchDocument)
            } else {
                removeBookmark(searchDocument)
            }
        }
    }
}

@AssistedFactory
interface SearchDocumentViewModelFactory {
    fun create(searchDocument: SearchDocument): SearchDocumentViewModel
}
