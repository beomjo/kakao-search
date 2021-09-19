package io.github.beomjo.search.mapper

import io.github.beomjo.search.datasource.local.table.VisitTable
import io.github.beomjo.search.entity.Visit

internal fun VisitTable.toEntity(): Visit {
    return Visit(
        url = url,
        date = date,
    )
}

internal fun Visit.toDocumentTable(): VisitTable {
    return VisitTable(
        url = url,
        date = date,
    )
}
