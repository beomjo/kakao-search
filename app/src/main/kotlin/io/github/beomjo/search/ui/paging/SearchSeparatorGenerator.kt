package io.github.beomjo.search.ui.paging

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.R
import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.ui.viewmodels.SearchViewModel
import io.github.beomjo.search.util.DateHelper

class SearchSeparatorGenerator @AssistedInject constructor(
    @Assisted val pagingData: PagingData<Document>,
    private val dateHelper: DateHelper
) {

    fun insertTitleSeparator(): PagingData<SearchViewModel.SearchUiItem> {
        return pagingData.map { document -> SearchViewModel.SearchUiItem.DocumentItem(document) }
            .insertSeparators { before, after ->
                if (after == null) return@insertSeparators null
                if (before == null) return@insertSeparators SearchViewModel.SearchUiItem.SeparatorItem(
                    "${after.document.title.first()}"
                )

                val beforeFirstWord = before.document.title.first()
                val afterFirstWord = after.document.title.first()
                return@insertSeparators when (beforeFirstWord != afterFirstWord) {
                    true -> SearchViewModel.SearchUiItem.SeparatorItem("${after.document.title.first()}")
                    else -> null
                }
            }
    }

    fun insertDateSeparator(): PagingData<SearchViewModel.SearchUiItem> {
        return pagingData.map { document -> SearchViewModel.SearchUiItem.DocumentItem(document) }
            .insertSeparators { before, after ->
                if (after == null) return@insertSeparators null
                if (before == null) return@insertSeparators SearchViewModel.SearchUiItem.SeparatorItem(
                    dateHelper.convert(after.document.date)
                )

                val beforeDate = before.document.date
                val afterDate = after.document.date

                val beforeDateString = dateHelper.convert(beforeDate, R.string.date_month)
                val afterDateString = dateHelper.convert(afterDate)
                return@insertSeparators when (beforeDateString != afterDateString) {
                    true -> SearchViewModel.SearchUiItem.SeparatorItem(description = afterDateString)
                    else -> null
                }
            }
    }
}

@AssistedFactory
interface SearchSeparatorFactory {
    fun create(pagingData: PagingData<Document>): SearchSeparatorGenerator
}

