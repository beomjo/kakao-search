package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.Empty
import io.github.beomjo.search.entity.History
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.usecase.base.FlowUseCase
import io.github.beomjo.search.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSearchHistoryList @Inject constructor(
    private val searchRepository: SearchRepository
) : FlowUseCase<Empty, List<History>>() {
    override  fun execute(parameters: Empty): Flow<List<History>> {
        return searchRepository.getSearchHistoryList()
    }
}