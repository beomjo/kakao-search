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

package io.github.beomjo.search.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.github.beomjo.search.R
import io.github.beomjo.search.base.BaseActivity
import io.github.beomjo.search.databinding.ActivityDetailBinding
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.ui.viewmodels.DetailViewModel

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private val detailViewModel: DetailViewModel by getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLayout()
    }

    private fun bindLayout() {
        binding {
            lifecycleOwner = this@DetailActivity
            detailViewModel = this@DetailActivity.detailViewModel.apply {
                init(intent.getParcelableExtra(KEY_DOCUMENT))
            }
            toolBar.setNavigationOnClickListener {
                onBackPressed()
            }
            moveWebviewBtn.setOnClickListener {
                this@DetailActivity.detailViewModel.run {
                    setVisit()
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(searchDocument.value?.url)
                        )
                    )
                }

            }
            favoriteBtn.setOnClickListener {
                favoriteBtn.isSelected = !favoriteBtn.isSelected
            }
        }
    }

    companion object {
        private const val KEY_DOCUMENT = "DetailActivity.Document"
        fun action(context: Context, searchDocument: SearchDocument) {
            Intent(context, DetailActivity::class.java)
                .apply { putExtra(KEY_DOCUMENT, searchDocument) }
                .let { context.startActivity(it) }
        }
    }
}