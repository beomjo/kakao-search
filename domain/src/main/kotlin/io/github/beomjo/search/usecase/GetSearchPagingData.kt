package io.github.beomjo.search.usecase

import androidx.paging.PagingData
import io.github.beomjo.search.entity.*
import io.github.beomjo.search.repository.DocumentRepository
import io.github.beomjo.search.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SearchPagingParam(
    val documentType: DocumentType,
    val sortType: SortType,
    val query: String,
    val sort: Sort = Sort.ACCURACY,
)

class GetSearchPagingData @Inject constructor(
    private val documentRepository: DocumentRepository
) : PagingUseCase<SearchPagingParam, Document>() {
    override fun execute(parameters: SearchPagingParam): Flow<PagingData<Document>> {
        return documentRepository.fetchDocumentPagingData(parameters)
    }
}