package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.Empty
import io.github.beomjo.search.entity.History
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.usecase.base.UseCase
import javax.inject.Inject


class GetSearchHistoryList @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCase<Empty, List<History>>() {
    override suspend fun execute(parameters: Empty): List<History> {
        return searchRepository.getSearchHistoryList()
    }
}