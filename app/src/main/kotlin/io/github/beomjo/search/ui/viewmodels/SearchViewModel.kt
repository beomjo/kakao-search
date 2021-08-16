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
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.usecase.GetSearchPagingData
import io.github.beomjo.search.usecase.SearchPagingParam
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val getSearchPagingData: GetSearchPagingData
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
            .map { pagingData -> pagingData.map { document -> SearchUiItem.DocumentItem(document) } }
            .map {
                it.insertSeparators<SearchUiItem.DocumentItem, SearchUiItem> { before, after ->
                    if (after == null) return@insertSeparators null
                    if (before == null) return@insertSeparators SearchUiItem.SeparatorItem("${after.document.title.first()}")

                    val beforeFirstWord = before.document.title.first()
                    val afterFirstWord = after.document.title.first()
                    return@insertSeparators when (beforeFirstWord.compareTo(afterFirstWord)) {
                        EQUAL, OVER -> null
                        UNDER -> SearchUiItem.SeparatorItem("${after.document.title.first()}")
                        else -> null
                    }
                }
            }
            .cachedIn(viewModelScope)
            .asLiveData()
    }


    sealed class SearchUiItem {
        data class DocumentItem(val document: Document) : SearchUiItem()
        data class SeparatorItem(val description: String) : SearchUiItem()
    }

    companion object {
        const val EQUAL = 0
        const val UNDER = -1
        const val OVER = 1
    }
}
