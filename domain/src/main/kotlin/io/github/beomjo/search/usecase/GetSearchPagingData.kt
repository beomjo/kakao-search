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

package io.github.beomjo.search.usecase

import androidx.paging.PagingData
import io.github.beomjo.search.entity.*
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import java.util.Date
import javax.inject.Inject

data class SearchPagingParam(
    val documentType: DocumentType,
    val sortType: SortType,
    val query: String,
    val sort: Sort = Sort.ACCURACY,
    val date: Date = Date()
)

class GetSearchPagingData @Inject constructor(
    private val searchRepository: SearchRepository
) : PagingUseCase<SearchPagingParam, SearchDocument>() {
    override fun execute(param: SearchPagingParam): Flow<PagingData<SearchDocument>> {
        return searchRepository.getDocumentPagingData(param)
            .onStart {
                searchRepository.insertSearchHistory(
                    History(
                        query = param.query,
                        date = param.date
                    )
                )
            }
    }
}
