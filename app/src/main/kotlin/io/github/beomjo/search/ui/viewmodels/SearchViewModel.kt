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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.usecase.GetSearchPagingData
import io.github.beomjo.search.usecase.SearchPagingParam
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val getSearchPagingData: GetSearchPagingData
) : BaseViewModel() {

    val query = MutableLiveData<String>()

    var searchFilter: DocumentType = DocumentType.ALL

    var sort: SortType = SortType.TITLE

    private val _pager = MutableLiveData<SearchPagingParam>()
    val pager: LiveData<PagingData<Document>> = _pager.switchMap(::getPager)

    private val _isShowProgress = MutableLiveData(false)
    val isShowProgress: LiveData<Boolean> get() = _isShowProgress

    fun search() {
        if (!query.value.isNullOrEmpty()) {
            _pager.value = getSearchPagingParam(query.value!!)
        }
    }

    private fun getSearchPagingParam(query: String): SearchPagingParam {
        return SearchPagingParam(
            documentType = searchFilter,
            sortType = sort,
            query = query
        )
    }

    private fun getPager(requestParam: SearchPagingParam): LiveData<PagingData<Document>> {
        return getSearchPagingData(requestParam)
            .cachedIn(viewModelScope)
            .asLiveData()
    }
}
