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
