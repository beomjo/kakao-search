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
import androidx.paging.PagingData
import androidx.paging.map
import io.github.beomjo.search.entity.*
import io.github.beomjo.search.ui.paging.SearchSeparatorFactory
import io.github.beomjo.search.ui.paging.SearchSeparatorGenerator
import io.github.beomjo.search.usecase.GetSearchHistoryList
import io.github.beomjo.search.usecase.GetSearchPagingData
import io.github.beomjo.search.usecase.SearchPagingParam
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.every
import io.mockk.slot
import io.mockk.just
import io.mockk.Runs
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class SearchViewModelSpec : BehaviorSpec() {

    private val getSearchPagingUseCase = mockk<GetSearchPagingData>(relaxed = true)

    private val searchHistoryListUseCase = mockk<GetSearchHistoryList>(relaxed = true)

    private val searchSeparatorFactory = mockk<SearchSeparatorFactory>(relaxed = true)

    init {
        Given("In Constructor") {

            val historyList = listOf(mockk<History>())

            every { searchHistoryListUseCase.invoke(Empty) } returns flowOf(historyList)


            When("Create a view model") {
                val viewModel = SearchViewModel(
                    getSearchPagingUseCase,
                    searchHistoryListUseCase,
                    searchSeparatorFactory,
                )

                Then("History list should be loaded") {
                    verify { searchHistoryListUseCase.invoke(eq(Empty)) }

                    viewModel.history.value?.shouldContainAll(historyList)
                }
            }
        }

        Given("Given query, sort, documentType") {
            val query = "IU"
            val searchFilter = DocumentType.ALL

            val pagingData = PagingData.from<Document>(
                listOf(mockk())
            )

            val insertedSeparatorPagingData = PagingData.from<SearchViewModel.SearchUiItem>(
                listOf(mockk())
            )

            val requestParamSlot = slot<SearchPagingParam>()
            every { getSearchPagingUseCase.invoke(capture(requestParamSlot)) } returns flowOf(
                pagingData
            )

            val searchHistoryListUseCase = mockk<GetSearchHistoryList>(relaxed = true)

            val separatorGenerator = mockk<SearchSeparatorGenerator> {
                every { insertTitleSeparator() } returns insertedSeparatorPagingData
                every { insertDateSeparator() } returns insertedSeparatorPagingData
            }

            every { searchSeparatorFactory.create(pagingData) } returns separatorGenerator

            val viewModel = SearchViewModel(
                getSearchPagingUseCase,
                searchHistoryListUseCase,
                searchSeparatorFactory,
            )

            viewModel.query.value = query
            viewModel.searchFilter = searchFilter

            val pagerDateSlot = slot<PagingData<SearchViewModel.SearchUiItem>>()
            val pagerObserver = mockk<Observer<PagingData<SearchViewModel.SearchUiItem>>> {
                every { onChanged(capture(pagerDateSlot)) } just Runs
            }
            viewModel.pager.observeForever(pagerObserver)

            val hasFocusObserver = mockk<Observer<Boolean>>(relaxed = true)
            viewModel.hasFocus.observeForever(hasFocusObserver)

            And("SortType is Title") {
                val sortType = SortType.TITLE
                viewModel.sort = sortType

                When("Do search") {
                    viewModel.search()

                    Then("Need to load PagingData") {
                        verify {
                            hasFocusObserver.onChanged(eq(false))
                            getSearchPagingUseCase.invoke(any())
                            searchSeparatorFactory.create(eq(pagingData))
                            separatorGenerator.insertTitleSeparator()
                        }
                        requestParamSlot.captured.documentType shouldBe searchFilter
                        requestParamSlot.captured.sortType shouldBe sortType
                        requestParamSlot.captured.query shouldBe query

                        val expect = mutableListOf<SearchViewModel.SearchUiItem>()
                        val actual = mutableListOf<SearchViewModel.SearchUiItem>()
                        pagerDateSlot.captured.map {
                            actual.add(it)
                        }
                        insertedSeparatorPagingData.map {
                            expect.add(it)
                        }
                        actual shouldContainAll expect
                    }
                }
            }

            And("SortType is DATE") {
                val sortType = SortType.DATE
                viewModel.sort = sortType

                When("Do search") {
                    viewModel.search()

                    Then("Need to load PagingData") {
                        verify {
                            hasFocusObserver.onChanged(eq(false))
                            getSearchPagingUseCase.invoke(any())
                            searchSeparatorFactory.create(eq(pagingData))
                            separatorGenerator.insertDateSeparator()
                        }
                        requestParamSlot.captured.documentType shouldBe searchFilter
                        requestParamSlot.captured.sortType shouldBe sortType
                        requestParamSlot.captured.query shouldBe query

                        val expect = mutableListOf<SearchViewModel.SearchUiItem>()
                        val actual = mutableListOf<SearchViewModel.SearchUiItem>()
                        pagerDateSlot.captured.map {
                            actual.add(it)
                        }
                        insertedSeparatorPagingData.map {
                            expect.add(it)
                        }
                        actual shouldContainAll expect
                    }
                }
            }
        }
    }
}
