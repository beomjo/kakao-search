package io.github.beomjo.search.mapper

import io.github.beomjo.search.datasource.local.table.SearchHistoryTable
import io.github.beomjo.search.entity.History

internal fun SearchHistoryTable.toEntity(): History {
    return History(
        query = query,
        date = date
    )
}

internal fun History.toTable(): SearchHistoryTable {
    return SearchHistoryTable(
        query = query,
        date = date
    )
}