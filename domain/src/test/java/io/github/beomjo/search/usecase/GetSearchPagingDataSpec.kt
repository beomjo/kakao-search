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
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.repository.SearchRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first

class GetSearchPagingDataSpec : BehaviorSpec() {

    private val searchRepository: SearchRepository = mockk()

    private val getSearchPagingData = GetSearchPagingData(searchRepository)

    init {
        Given("SearchPagingParam") {
            val param = SearchPagingParam(
                documentType = DocumentType.ALL,
                sortType = SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY
            )
            val pagingData = mockk<PagingData<SearchDocument>>()
            coEvery { searchRepository.getDocumentPagingData(param) } returns flowOf(pagingData)
            coEvery { searchRepository.insertSearchHistory(any()) } just Runs

            When("Call invoke") {
                val resultFlow = getSearchPagingData.invoke(param)

                Then("Return PagingData") {
                    resultFlow.first() shouldBe pagingData

                    coVerify { searchRepository.getDocumentPagingData(eq(param)) }
                    coVerify { searchRepository.insertSearchHistory(any()) }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
