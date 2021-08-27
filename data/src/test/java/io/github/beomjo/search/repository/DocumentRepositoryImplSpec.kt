package io.github.beomjo.search.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import io.github.beomjo.search.datasource.local.dao.DocumentDao
import io.github.beomjo.search.datasource.local.table.DocumentTable
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediator
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediatorFactory
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.usecase.SearchPagingParam
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.*
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
internal class DocumentRepositoryImplSpec : BehaviorSpec() {

    private val searchRemoteMediatorFactory = mockk<SearchRemoteMediatorFactory>()

    private val searchRemoteMediator = mockk<SearchRemoteMediator>(relaxed = true)

    private val pagingSource = mockk<PagingSource<Int, DocumentTable>>(relaxed = true)

    private val documentDao = mockk<DocumentDao>()

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

            val documentRepositoryImpl = DocumentRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao
            )

            When("invoke fetchPagingData") {
                val resultFlow = documentRepositoryImpl.fetchDocumentPagingData(param)

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

            val documentRepositoryImpl = DocumentRepositoryImpl(
                searchRemoteMediatorFactory,
                documentDao
            )

            When("invoke fetchPagingData") {
                val resultFlow = documentRepositoryImpl.fetchDocumentPagingData(param)

                Then("Returns PagingData sorted by DATE") {
                    resultFlow.first().shouldBeTypeOf<PagingData<Document>>()

                    coVerify(inverse = true) { documentDao.getDocumentByTitle(any()) }
                    coVerify { documentDao.getDocumentByDate(eq(param.query)) }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}