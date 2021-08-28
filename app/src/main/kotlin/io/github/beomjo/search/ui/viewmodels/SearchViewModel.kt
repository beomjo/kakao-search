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
import io.github.beomjo.search.ui.paging.SearchSeparatorFactory
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchPagingData: GetSearchPagingData,
    private val searchSeparatorFactory: SearchSeparatorFactory
) : BaseViewModel() {

    val query = MutableLiveData<String>()

    var searchFilter: DocumentType = DocumentType.ALL

    var sort: SortType = SortType.TITLE

    private val _pager = MutableLiveData<SearchPagingParam>()
    val pager: LiveData<PagingData<SearchUiItem>> = _pager.switchMap(::getPager)

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

    private fun getPager(requestParam: SearchPagingParam): LiveData<PagingData<SearchUiItem>> {
        return getSearchPagingData(requestParam)
            .map { pagingData ->
                when (sort) {
                    SortType.TITLE -> searchSeparatorFactory.create(pagingData)
                        .insertTitleSeparator()
                    else -> searchSeparatorFactory.create(pagingData).insertDateSeparator()
                }
            }
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    sealed class SearchUiItem {
        data class DocumentItem(val document: Document) : SearchUiItem()
        data class SeparatorItem(val description: String) : SearchUiItem()
    }
}
