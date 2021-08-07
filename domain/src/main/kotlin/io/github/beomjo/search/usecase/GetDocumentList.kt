package io.github.beomjo.search.usecase

import io.github.beomjo.search.entity.DocumentList
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.repository.DocumentRepository
import javax.inject.Inject

class GetDocumentList @Inject constructor(
    private val documentRepository: DocumentRepository
) {

    suspend operator fun invoke(
        documentType: DocumentType,
        sortType: SortType,
        query: String,
        sort: Sort = Sort.ACCURACY,
        page: Int,
    ): DocumentList {
        val documentList = when (documentType) {
            DocumentType.ALL -> {
                val blogDocumentList = documentRepository.fetchBlog(query, sort, page, size = 5)
                val cafeDocumentList = documentRepository.fetchCafe(query, sort, page, size = 5)
                val videoDocumentList = documentRepository.fetchVideo(query, sort, page, size = 5)
                val imageDocumentList = documentRepository.fetchImages(query, sort, page, size = 5)
                val bookDocumentList = documentRepository.fetchBook(query, sort, page, size = 5)

                blogDocumentList + cafeDocumentList + videoDocumentList + imageDocumentList + bookDocumentList
            }
            DocumentType.BLOG -> {
                documentRepository.fetchBlog(query, sort, page, size = 25)
            }
            DocumentType.CAFE -> {
                documentRepository.fetchCafe(query, sort, page, size = 25)
            }
            DocumentType.VIDEO -> {
                documentRepository.fetchVideo(query, sort, page, size = 25)
            }
            DocumentType.IMAGE -> {
                documentRepository.fetchImages(query, sort, page, size = 25)
            }
            DocumentType.BOOK -> {
                documentRepository.fetchBook(query, sort, page, size = 25)
            }
        }

        return documentList.copy(
            documents = when (sortType) {
                SortType.TITLE -> documentList.documents.sortedBy { it.title }
                else -> documentList.documents.sortedByDescending { it.date }
            }
        )
    }
}