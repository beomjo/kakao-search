package io.github.beomjo.search.ui.paging

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.R
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.ui.viewmodels.SearchViewModel
import io.github.beomjo.search.util.DateHelper

class SearchSeparatorGenerator @AssistedInject constructor(
    @Assisted val pagingData: PagingData<SearchDocument>,
    private val dateHelper: DateHelper
) {

    fun insertTitleSeparator(): PagingData<SearchViewModel.SearchUiItem> {
        return pagingData.map { document -> SearchViewModel.SearchUiItem.DocumentItem(document) }
            .insertSeparators { before, after ->
                if (after == null) return@insertSeparators null
                if (before == null) return@insertSeparators SearchViewModel.SearchUiItem.SeparatorItem(
                    "${after.searchDocument.title.first()}"
                )

                val beforeFirstWord = before.searchDocument.title.first()
                val afterFirstWord = after.searchDocument.title.first()
                return@insertSeparators when (beforeFirstWord != afterFirstWord) {
                    true -> SearchViewModel.SearchUiItem.SeparatorItem("${after.searchDocument.title.first()}")
                    else -> null
                }
            }
    }

    fun insertDateSeparator(): PagingData<SearchViewModel.SearchUiItem> {
        return pagingData.map { document -> SearchViewModel.SearchUiItem.DocumentItem(document) }
            .insertSeparators { before, after ->
                if (after == null) return@insertSeparators null
                if (before == null) return@insertSeparators SearchViewModel.SearchUiItem.SeparatorItem(
                    dateHelper.convert(after.searchDocument.date)
                )

                val beforeDate = before.searchDocument.date
                val afterDate = after.searchDocument.date

                val beforeDateString = dateHelper.convert(beforeDate, R.string.date_month)
                val afterDateString = dateHelper.convert(afterDate, R.string.date_month)
                return@insertSeparators when (beforeDateString != afterDateString) {
                    true -> SearchViewModel.SearchUiItem.SeparatorItem(description = afterDateString)
                    else -> null
                }
            }
    }
}

@AssistedFactory
interface SearchSeparatorFactory {
    fun create(pagingData: PagingData<SearchDocument>): SearchSeparatorGenerator
}
