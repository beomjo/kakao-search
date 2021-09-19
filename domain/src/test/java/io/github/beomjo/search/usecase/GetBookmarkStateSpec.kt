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

import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.repository.BookmarkRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import io.mockk.verify

class GetBookmarkStateSpec : BehaviorSpec() {

    private val bookmarkRepository = mockk<BookmarkRepository>(relaxed = true)

    init {
        Given("Given a searchDocument") {
            val searchDocument = mockk<SearchDocument>()
            val getBookmarkUseCase = GetBookmarkState(bookmarkRepository)

            When("Call invoke") {
                getBookmarkUseCase.invoke(searchDocument)

                Then("Should return whether or not it has been bookmarked") {
                    verify {
                        bookmarkRepository.isBookmarked(eq(searchDocument))
                    }
                }
            }
        }
    }
}
