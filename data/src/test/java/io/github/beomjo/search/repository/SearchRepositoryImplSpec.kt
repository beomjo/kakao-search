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

package io.github.beomjo.search.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import io.github.beomjo.search.datasource.local.dao.DocumentDao
import io.github.beomjo.search.datasource.local.dao.SearchHistoryDao
import io.github.beomjo.search.datasource.local.table.DocumentTable
import io.github.beomjo.search.datasource.local.table.SearchHistoryTable
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediator
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediatorFactory
import io.github.beomjo.search.entity.*
import io.github.beomjo.search.mapper.toEntity
import io.github.beomjo.search.mapper.toTable
import io.github.beomjo.search.usecase.SearchPagingParam
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.mockk
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
internal class SearchRepositoryImplSpec : BehaviorSpec() {

    private val searchRemoteMediatorFactory = mockk<SearchRemoteMediatorFactory>()

    private val searchRemoteMediator = mockk<SearchRemoteMediator>(relaxed = true)

    private val pagingSource = mockk<PagingSource<Int, DocumentTable>>(relaxed = true)

    private val documentDao = mockk<DocumentDao>(relaxed = true)

    private val searchHistoryDao = mockk<SearchHistoryDao>(relaxed = true)

    init {

        Given("Given the SortType.TITLE") {
            val param = SearchPagingParam(
                documentType = DocumentType.ALL,
                sortType = SortType.TITLE,
                sort = Sort.ACCURACY,
                query = "IU"
            )

            every { searchRemoteMediatorFactory.create(param) } returns searchRemoteMediator

            coEvery { documentDao.getDocumentByTitle(param.query) } returns pagingSource

            val searchRepositoryImpl = SearchRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao,
                searchHistoryDao
            )

            When("invoke fetchPagingData") {
                val resultFlow = searchRepositoryImpl.getDocumentPagingData(param)

                Then("Returns PagingData sorted by TITLE") {
                    resultFlow.first().shouldBeTypeOf<PagingData<Document>>()

                    coVerify { documentDao.getDocumentByTitle(eq(param.query)) }
                    coVerify(inverse = true) { documentDao.getDocumentByDate(any()) }
                }
            }
        }

        Given("Given the SortType.DATE") {
            val param = SearchPagingParam(
                documentType = DocumentType.ALL,
                sortType = SortType.DATE,
                sort = Sort.ACCURACY,
                query = "IU"
            )

            every { searchRemoteMediatorFactory.create(param) } returns searchRemoteMediator

            coEvery { documentDao.getDocumentByDate(param.query) } returns pagingSource

            val searchRepositoryImpl = SearchRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao,
                searchHistoryDao
            )

            When("invoke fetchPagingData") {
                val resultFlow = searchRepositoryImpl.getDocumentPagingData(param)

                Then("Returns PagingData sorted by DATE") {
                    resultFlow.first().shouldBeTypeOf<PagingData<Document>>()

                    coVerify(inverse = true) { documentDao.getDocumentByTitle(any()) }
                    coVerify { documentDao.getDocumentByDate(eq(param.query)) }
                }
            }
        }

        Given("Given the search query keyword") {
            val history = History(
                "IU",
                mockk(),
            )

            val searchRepositoryImpl = SearchRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao,
                searchHistoryDao
            )

            When("When you call insert history to DB") {
                searchRepositoryImpl.insertSearchHistory(history)

                Then("Should be stored in DB") {
                    coVerify(inverse = false) { searchHistoryDao.insertHistory(eq(history.toTable())) }
                }
            }
        }

        Given("Nothing needs to be given") {
            val searchTableList = listOf(
                mockk<SearchHistoryTable> {
                    every { query } returns "IU"
                    every { date } returns mockk()
                }
            )

            coEvery { searchHistoryDao.getHistoryList() } returns searchTableList

            val searchRepositoryImpl = SearchRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao,
                searchHistoryDao
            )

            When("When you call insert history to DB") {
                val result = searchRepositoryImpl.getSearchHistoryList()

                Then("Should be stored in DB") {
                    coVerify(inverse = false) { searchHistoryDao.getHistoryList() }
                    result shouldBe searchTableList.map { it.toEntity() }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
