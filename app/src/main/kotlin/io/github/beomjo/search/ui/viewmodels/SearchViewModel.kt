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

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.usecase.GetSearchPagingData
import io.github.beomjo.search.usecase.SearchPagingParam
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val getSearchPagingData: GetSearchPagingData
) : ViewModel() {

    private val _documentType = MutableLiveData(DEFAULT_DOCUMENT_TYPE)
    val documentType: LiveData<DocumentType> get() = _documentType

    private val _sortType = MutableLiveData(DEFAULT_SORT_TYPE)
    val sortType: LiveData<SortType> get() = _sortType

    private val _pager = MutableLiveData<SearchPagingParam>()
    val pager: LiveData<PagingData<Document>> = _pager.switchMap(::getPager)

    fun search(query: String) {
        _pager.value = getSearchPagingParam(query)
    }

    private fun getSearchPagingParam(query: String): SearchPagingParam {
        return SearchPagingParam(
            documentType = documentType.value ?: DEFAULT_DOCUMENT_TYPE,
            sortType = sortType.value ?: DEFAULT_SORT_TYPE,
            query = query
        )
    }

    private fun getPager(requestParam: SearchPagingParam): LiveData<PagingData<Document>> {
        return getSearchPagingData(requestParam).cachedIn(viewModelScope).asLiveData()
    }

    companion object {
        private val DEFAULT_DOCUMENT_TYPE = DocumentType.ALL
        private val DEFAULT_SORT_TYPE = SortType.TITLE
    }
}