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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.beomjo.search.R
import io.github.beomjo.search.base.BaseFragment
import io.github.beomjo.search.databinding.FragmentBookmarkBinding
import io.github.beomjo.search.ui.activity.DetailActivity
import io.github.beomjo.search.ui.adapter.BookmarkPagingAdapter
import io.github.beomjo.search.ui.adapter.BookmarkPagingAdapterFactory
import io.github.beomjo.search.ui.viewmodels.BookmarkViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val bookmarkViewModel: BookmarkViewModel by getViewModel()

    @Inject
    lateinit var bookmarkPagingAdapterFactory: BookmarkPagingAdapterFactory

    private val bookmarkAdapter: BookmarkPagingAdapter by lazy {
        bookmarkPagingAdapterFactory.create(
            this,
            onClickItem = { searchDocument ->
                DetailActivity.action(requireActivity(), searchDocument)
            },
        )
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
            viewModel = bookmarkViewModel.apply { init() }
            bookmarkAdapter = this@BookmarkFragment.bookmarkAdapter
        }
    }

    private fun observeViewModel() {
        bookmarkViewModel.pager.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                bookmarkAdapter.submitData(it ?: return@launch)
            }
        }
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarkList ->
            bookmarkViewModel.removeBookmarkFromPagingData(bookmarkList)
            if (bookmarkAdapter.itemCount < bookmarkList.size) {
                bookmarkViewModel.refresh()
            }
        }
    }

    companion object {
        val TAG = BookmarkFragment::class.java.simpleName
    }
}
