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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.beomjo.search.R
import io.github.beomjo.search.base.BaseFragment
import io.github.beomjo.search.databinding.FragmentSearchBinding
import io.github.beomjo.search.ui.adapter.SearchPagingAdapter
import io.github.beomjo.search.ui.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by getViewModel()

    private val searchPagingAdapter: SearchPagingAdapter by lazy { SearchPagingAdapter() }

    override val viewModelProvideOwner: ViewModelStoreOwner
        get() = this.requireActivity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLayout()
        observeViewModel()
    }

    private fun bindLayout() {
        binding.apply {
            viewModel = searchViewModel
            adapter = searchPagingAdapter
        }
    }

    private fun observeViewModel() {
        searchViewModel.pager.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                searchPagingAdapter.submitData(it)
            }
        })
    }
}
