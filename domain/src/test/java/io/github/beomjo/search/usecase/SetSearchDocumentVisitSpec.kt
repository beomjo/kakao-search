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

class SetSearchDocumentVisitSpec : BehaviorSpec() {

    private val searchRepository = mockk<SearchRepository>()

    init {
        Given("Given a url") {
            val url = "http://..."
            val useCase = SetSearchDocumentVisit(searchRepository)
            val slot = slot<Visit>()
            coEvery { searchRepository.insertVisit(capture(slot)) } just Runs

            When("Invoke") {
                useCase.invoke(url)

                Then("Should call setVisit of SearchRepository") {
                    coEvery { searchRepository.insertVisit(any()) }
                    slot.captured.url shouldBe url
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}