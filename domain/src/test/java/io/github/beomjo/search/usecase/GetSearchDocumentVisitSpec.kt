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

import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.repository.SearchRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetSearchDocumentVisitSpec : BehaviorSpec() {

    private val searchRepository = mockk<SearchRepository>()

    init {
        Given("Given a url") {
            val expectUrl = "http://"
            val visit = mockk<Visit> {
                every { url } returns expectUrl
            }
            val useCase = GetSearchDocumentVisit(searchRepository)
            every { searchRepository.getVisit(expectUrl) } returns flowOf(visit)

            When("Invoke") {
                val result = useCase.invoke(expectUrl)

                Then("Should return a visit") {
                    verify { searchRepository.getVisit(eq(expectUrl)) }
                    result.first()?.url shouldBe expectUrl
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}