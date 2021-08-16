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

package io.github.beomjo.search.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.R
import io.github.beomjo.search.databinding.DocumentListFooterItemBinding

class DocumentLoadStateViewHolder(
    private val binding: DocumentListFooterItemBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            retryBtn.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
            retryBtn.setOnClickListener {
                retry.invoke()
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): DocumentLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.document_list_footer_item, parent, false)
            val binding = DocumentListFooterItemBinding.bind(view)
            return DocumentLoadStateViewHolder(binding, retry)
        }
    }
}
