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

import io.github.beomjo.search.entity.Empty
import io.github.beomjo.search.entity.History
import io.github.beomjo.search.repository.SearchRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetSearchHistoryListSpec : BehaviorSpec() {

    private val searchRepository = mockk<SearchRepository>()

    init {
        Given("use case is ") {
            val getSearchHistoryList = GetSearchHistoryList(searchRepository)

            val expect = listOf(mockk<History>())

            coEvery { searchRepository.getSearchHistoryList() } returns flowOf(expect)

            When("invoke") {
                val result = getSearchHistoryList.invoke(Empty)

                Then("Should return a HistoryList") {
                    coVerify { searchRepository.getSearchHistoryList() }
                    result.first() shouldBe expect
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
