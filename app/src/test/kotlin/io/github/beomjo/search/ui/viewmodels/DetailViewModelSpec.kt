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
import com.beomjo.compilation.util.Event
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.GetBookmarkState
import io.github.beomjo.search.usecase.RemoveBookmark
import io.github.beomjo.search.usecase.SetBookmark
import io.github.beomjo.search.usecase.SetSearchDocumentVisit
import io.kotest.core.spec.style.BehaviorSpec

import io.mockk.*
import kotlinx.coroutines.flow.flowOf

class DetailViewModelSpec : BehaviorSpec() {

    private val setSearchDocumentVisit = mockk<SetSearchDocumentVisit>(relaxed = true)

    private val getBookmarkState = mockk<GetBookmarkState>(relaxed = true)

    private val removeBookmark = mockk<RemoveBookmark>(relaxed = true)

    private val setBookmark = mockk<SetBookmark>(relaxed = true)

    private val searchDocumentObserver = mockk<Observer<SearchDocument>>(relaxed = true)

    private val isBookmarkedObserver = mockk<Observer<Boolean>>(relaxed = true)

    init {
        Given("Given a searchDocument") {
            val searchDocument = mockk<SearchDocument>()
            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )
            detailViewModel.searchDocument.observeForever(searchDocumentObserver)

            When("Call init") {
                detailViewModel.init(searchDocument)

                Then("Should to update searchDocument") {
                    verify { searchDocumentObserver.onChanged(eq(searchDocument)) }
                }
            }
        }

        Given("Search document given as null") {
            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )

            val eventObserver = mockk<Observer<Event<BaseViewModel.Action>>>(relaxed = true)
            detailViewModel.event.observeForever(eventObserver)

            When("Call init") {
                detailViewModel.init(null)

                Then("Should to update searchDocument") {
                    verify(inverse = true) { searchDocumentObserver.onChanged(any()) }
                    verify { eventObserver.onChanged(any<Event<BaseViewModel.Action.Finish>>()) }
                }
            }
        }

        Given("Given a search document") {
            val documentUrl = "http://.."
            val searchDocument = mockk<SearchDocument> {
                every { url } returns documentUrl
            }
            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )
            detailViewModel.init(searchDocument)

            When("Set visit search document") {
                detailViewModel.setVisit()

                Then("Should call setVisit to save whether searchDocument is visited or not") {
                    coVerify { setSearchDocumentVisit.invoke(eq(documentUrl)) }
                }
            }
        }

        Given("Given Search document is null") {
            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )

            When("Set visit search document") {
                detailViewModel.setVisit()

                Then("Should not call setSearchDocumentVisit") {
                    coVerify(inverse = true) { setSearchDocumentVisit.invoke(any()) }
                }
            }
        }

        Given("Given a not bookmarked") {
            val searchDocument = mockk<SearchDocument>()

            every { getBookmarkState.invoke(any()) } returns flowOf(false)

            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )
            detailViewModel.isBookmark.observeForever(isBookmarkedObserver)
            detailViewModel.init(searchDocument)

            When("Call onClickBookmark") {
                detailViewModel.onClickBookmark()

                Then("Should set a bookmark") {
                    verify {
                        isBookmarkedObserver.onChanged(eq(false))
                        getBookmarkState.invoke(eq(searchDocument))
                    }
                    coVerify { setBookmark(eq(searchDocument)) }
                }
            }
        }

        Given("Given a bookmarked") {
            val searchDocument = mockk<SearchDocument>()

            every { getBookmarkState.invoke(any()) } returns flowOf(true)

            val detailViewModel = DetailViewModel(
                setSearchDocumentVisit,
                getBookmarkState,
                removeBookmark,
                setBookmark
            )
            detailViewModel.isBookmark.observeForever(isBookmarkedObserver)
            detailViewModel.init(searchDocument)

            When("Call onClickBookmark") {
                detailViewModel.onClickBookmark()

                Then("Should set a bookmark") {
                    verify {
                        isBookmarkedObserver.onChanged(eq(true))
                        getBookmarkState.invoke(eq(searchDocument))
                    }
                    coVerify { removeBookmark(eq(searchDocument)) }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
