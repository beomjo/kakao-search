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

package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.Observer
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.usecase.GetSearchDocumentVisit
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import kotlinx.coroutines.flow.flowOf

class SearchDocumentViewModelSpec : BehaviorSpec() {

    private val getSearchDocumentVisit = mockk<GetSearchDocumentVisit>(relaxed = true)

    private val isVisitObserver = mockk<Observer<Boolean>>(relaxed = true)

    init {
        Given("Give a SearchDocument") {
            val documentUrl = "http://"
            val searchDocument = mockk<SearchDocument> {
                every { url } returns documentUrl
                every { date } returns mockk()
            }
            val visit = Visit(
                mockk(),
                documentUrl
            )
            every { getSearchDocumentVisit.invoke(documentUrl) } returns flowOf(visit)

            When("Create a ViewModel") {
                val viewModel = SearchDocumentViewModel(
                    searchDocument,
                    getSearchDocumentVisit
                )
                viewModel.isVisit.observeForever(isVisitObserver)

                Then("isVisit value needs to be updated, true") {
                    verify {
                        getSearchDocumentVisit.invoke(eq(documentUrl))
                        isVisitObserver.onChanged(eq(true))
                    }
                }
            }
        }

        Given("There is no search document") {
            val documentUrl = "http://"
            val searchDocument = mockk<SearchDocument> {
                every { url } returns documentUrl
                every { date } returns mockk()
            }
            every { getSearchDocumentVisit.invoke(any()) } returns flowOf(null)

            When("Create a ViewModel") {
                val viewModel = SearchDocumentViewModel(
                    searchDocument,
                    getSearchDocumentVisit
                )
                viewModel.isVisit.observeForever(isVisitObserver)

                Then("isVisit value needs to be updated, false") {
                    verify {
                        getSearchDocumentVisit.invoke(eq(documentUrl))
                        isVisitObserver.onChanged(eq(false))
                    }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}