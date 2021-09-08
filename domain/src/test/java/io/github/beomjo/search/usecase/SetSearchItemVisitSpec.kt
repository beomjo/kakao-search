package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.repository.SearchRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class SetSearchItemVisitSpec : BehaviorSpec() {

    private val searchRepository = mockk<SearchRepository>()

    init {
        Given("Given a url") {
            val url = "http://..."
            val useCase = SetSearchItemVisit(searchRepository)
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