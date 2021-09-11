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