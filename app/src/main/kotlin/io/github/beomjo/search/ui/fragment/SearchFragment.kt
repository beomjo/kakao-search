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

package io.github.beomjo.search.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.github.beomjo.search.R
import io.github.beomjo.search.base.BaseFragment
import io.github.beomjo.search.databinding.FragmentSearchBinding
import io.github.beomjo.search.ui.adapter.SearchControlMenuAdapter
import io.github.beomjo.search.ui.adapter.SearchHistoryAdapter
import io.github.beomjo.search.ui.adapter.SearchPagingAdapter
import io.github.beomjo.search.ui.adapter.SearchPagingLoadStateAdapter
import io.github.beomjo.search.ui.viewmodels.SearchViewModel
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by getViewModel()

    private val searchPagingAdapter: SearchPagingAdapter by lazy {
        SearchPagingAdapter {
            SearchFragmentDirections.actionSearchDestToDetailDest().let {
                findNavController().navigate(it)
            }
        }
    }

    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter {
            searchViewModel.query.value = it
            searchViewModel.search()
        }
    }

    override val viewModelProvideOwner: ViewModelStoreOwner
        get() = this.requireActivity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLayout()
        observeViewModel()
    }

    private fun bindLayout() {
        binding {
            viewModel = searchViewModel
            searchAdapter = ConcatAdapter(
                ConcatAdapter.Config.DEFAULT,
                SearchControlMenuAdapter(
                    onFilterSelected = {
                        searchViewModel.searchFilter = it
                        searchViewModel.search()
                    },
                    onSortSelected = {
                        searchViewModel.sort = it
                        searchViewModel.search()
                    },
                ),
                searchPagingAdapter
                    .apply {
                        addLoadStateListener { loadState ->
                            val isLoading = loadState.mediator?.refresh is LoadState.Loading
                            val isError = loadState.mediator?.refresh is LoadState.Error
                            val isEmptyItem = searchPagingAdapter.itemCount == 0
                            emptyLayout.isVisible = isEmptyItem && !isError && !isLoading
                            recyclerview.isVisible = searchPagingAdapter.itemCount > 0
                            progressBar.isVisible = isLoading && isEmptyItem
                            retryButton.apply {
                                isVisible = isError
                                setOnClickListener {
                                    searchPagingAdapter.retry()
                                }
                            }
                        }
                    }
                    .withLoadStateFooter(
                        SearchPagingLoadStateAdapter {
                            searchPagingAdapter.retry()
                        }
                    ),
            )

            editSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewModel.search()
                    true
                } else false
            }

            searchHistoryAdapter = this@SearchFragment.searchHistoryAdapter
        }
    }

    private fun observeViewModel() {
        searchViewModel.pager.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                searchPagingAdapter.submitData(it)
            }
        })

        searchViewModel.history.observe(viewLifecycleOwner, {
            searchHistoryAdapter.submitList(it)
        })
    }

    companion object {
        val TAG = SearchFragment::class.java.simpleName
    }
}
