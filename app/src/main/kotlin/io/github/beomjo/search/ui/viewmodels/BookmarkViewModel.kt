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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.Empty
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.GetBookmarkList
import io.github.beomjo.search.usecase.GetBookmarkPagingData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarkPagingData: GetBookmarkPagingData,
    private val getBookmarkList: GetBookmarkList,
) : BaseViewModel() {

    private val _pager = MutableLiveData<PagingData<SearchDocument>>()
    val pager: LiveData<PagingData<SearchDocument>> get() = _pager

    private val _isRefresh = MutableLiveData(false)
    val isRefresh: LiveData<Boolean> get() = _isRefresh

    val bookmarkList: LiveData<List<SearchDocument>> = getBookmarkList(Empty).asLiveData()

    fun init() {
        viewModelScope.launch {
            getBookmarkPagingData(Empty)
                .onEach { _isRefresh.value = false }
                .cachedIn(viewModelScope)
                .collect { _pager.value = it }
        }
    }

    fun refresh() {
        init()
    }

    fun removeBookmarkFromPagingData(bookmarkList: List<SearchDocument>) {
        pager.value
            ?.filter { bookmarkList.contains(it) }
            ?.let { _pager.value = it }
    }
}
