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
import io.github.beomjo.search.entity.Empty
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.repository.BookmarkRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetBookmarkPagingDataSpec : BehaviorSpec() {

    private val bookmarkRepository = mockk<BookmarkRepository>()

    init {
        Given("Given a Empty") {
            val searchDocumentList = listOf<SearchDocument>(mockk(), mockk())
            val pagingData = PagingData.from(searchDocumentList)
            val getBookmarkPagingDataUseCase = GetBookmarkPagingData(bookmarkRepository)
            every { bookmarkRepository.getBookmarkPagingData() } returns flowOf(pagingData)

            When("Call invoke") {
                val bookmarkListFlow = getBookmarkPagingDataUseCase.invoke(Empty)

                Then("Should return a bookmark list flow") {
                    bookmarkListFlow.first() shouldBe pagingData
                    verify { bookmarkRepository.getBookmarkPagingData() }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}