package io.github.beomjo.search.usecase

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.repository.DocumentRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.fp.success
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetSearchPagingDataSpec : BehaviorSpec({

    val documentRepository: DocumentRepository = mockk()

    val getSearchPagingData = GetSearchPagingData(documentRepository)

    Given("SearchPagingParam") {
        val param = SearchPagingParam(
            documentType = DocumentType.ALL,
            sortType = SortType.TITLE,
            query = "IU",
            sort = Sort.ACCURACY
        )
        val pagingData = mockk<PagingData<Document>> {}
        coEvery { documentRepository.fetchDocumentPagingData(param) } returns flowOf(pagingData)

        When("invoke") {
            val result = getSearchPagingData.invoke(param)

            Then("Return PagingData") {
                coVerify { documentRepository.fetchDocumentPagingData(eq(param)) }
                result shouldBe pagingData
            }
        }
    }

    afterTest {
        unmockkAll()
    }
})