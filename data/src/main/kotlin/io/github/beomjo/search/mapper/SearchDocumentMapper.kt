/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.beomjo.search.mapper

import io.github.beomjo.search.datasource.local.table.SearchDocumentTable
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.entity.SearchDocumentList
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.model.BlogResponse
import io.github.beomjo.search.model.BookResponse
import io.github.beomjo.search.model.CafeResponse
import io.github.beomjo.search.model.DocumentListResponse
import io.github.beomjo.search.model.ImageResponse
import io.github.beomjo.search.model.WebResponse
import java.lang.IllegalStateException

internal fun <T> DocumentListResponse<T>.toEntity(): SearchDocumentList {
    return SearchDocumentList(
        hasMore = !meta.isEnd,
        searchDocuments = documents.map {
            when (it) {
                is BlogResponse -> it.toEntity()
                is CafeResponse -> it.toEntity()
                is ImageResponse -> it.toEntity()
                is WebResponse -> it.toEntity()
                is BookResponse -> it.toEntity()
                else -> throw IllegalStateException("Not Exist Model Receive")
            }
        }.toList()
    )
}

internal fun BlogResponse.toEntity(): SearchDocument = SearchDocument(
    type = DocumentType.BLOG,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = contents,
    date = datetime,
)

internal fun CafeResponse.toEntity(): SearchDocument = SearchDocument(
    type = DocumentType.CAFE,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = contents,
    date = datetime,
)

internal fun ImageResponse.toEntity(): SearchDocument = SearchDocument(
    type = DocumentType.IMAGE,
    url = docUrl,
    thumbnail = thumbnailUrl,
    title = "$displaySiteName-$collection",
    content = "",
    date = datetime,
)

internal fun WebResponse.toEntity(): SearchDocument = SearchDocument(
    type = DocumentType.WEB,
    url = url,
    thumbnail = "",
    title = title,
    content = contents,
    date = datetime,
)

internal fun BookResponse.toEntity(): SearchDocument = SearchDocument(
    type = DocumentType.BOOK,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = "${authors.joinToString(",")}\n${translators.joinToString(",")}\n${publisher}\n$price",
    date = datetime,
)

internal fun SearchDocument.toTable(): SearchDocumentTable {
    return SearchDocumentTable(
        type = this.type,
        url = this.url,
        thumbnail = this.thumbnail,
        title = this.title,
        content = this.content,
        date = this.date
    )
}

internal fun SearchDocumentTable.toEntity(): SearchDocument {
    return SearchDocument(
        type = this.type,
        url = this.url,
        thumbnail = this.thumbnail,
        title = this.title,
        content = this.content,
        date = this.date
    )
}
