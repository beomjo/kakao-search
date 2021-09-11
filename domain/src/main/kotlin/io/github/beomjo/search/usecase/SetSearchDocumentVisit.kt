package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.usecase.base.UseCase
import java.util.*
import javax.inject.Inject

class SetSearchDocumentVisit @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCase<String, Unit>() {
    override suspend fun execute(parameters: String) {
        searchRepository.insertVisit(Visit(Date(), parameters))
    }
}