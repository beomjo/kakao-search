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
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.repository.DocumentRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

import io.mockk.coEvery
import io.mockk.unmockkAll
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first

class GetSearchPagingDataSpec : BehaviorSpec() {

    private val documentRepository: DocumentRepository = mockk()

    private val getSearchPagingData = GetSearchPagingData(documentRepository)

    init {
        Given("SearchPagingParam") {
            val param = SearchPagingParam(
                documentType = DocumentType.ALL,
                sortType = SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY
            )
            val pagingData = mockk<PagingData<Document>>()
            coEvery { documentRepository.fetchDocumentPagingData(param) } returns flowOf(pagingData)

            When("invoke") {
                val resultFlow = getSearchPagingData.invoke(param)

                Then("Return PagingData") {
                    coVerify { documentRepository.fetchDocumentPagingData(eq(param)) }
                    resultFlow.first() shouldBe pagingData
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
