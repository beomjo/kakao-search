package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchItemVisit @Inject constructor(
    private val searchRepository: SearchRepository
) : FlowUseCase<String, Visit?>() {
    override fun execute(parameters: String): Flow<Visit?> {
        return searchRepository.getVisit(parameters)
    }
}