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

import io.github.beomjo.search.entity.Document
import io.github.beomjo.search.entity.DocumentList
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.model.ModelBlog
import io.github.beomjo.search.model.ModelBook
import io.github.beomjo.search.model.ModelCafe
import io.github.beomjo.search.model.ModelDocumentList
import io.github.beomjo.search.model.ModelImage
import io.github.beomjo.search.model.ModelWeb
import java.lang.IllegalStateException

internal fun <T> ModelDocumentList<T>.toEntity(): DocumentList {
    return DocumentList(
        hasMore = !meta.isEnd,
        documents = documents.map {
            when (it) {
                is ModelBlog -> it.toEntity()
                is ModelCafe -> it.toEntity()
                is ModelImage -> it.toEntity()
                is ModelWeb -> it.toEntity()
                is ModelBook -> it.toEntity()
                else -> throw IllegalStateException("Not Exist Model Receive")
            }
        }.toList()
    )
}

internal fun ModelBlog.toEntity(): Document = Document(
    type = DocumentType.BLOG,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = contents,
    date = datetime,
)

internal fun ModelCafe.toEntity(): Document = Document(
    type = DocumentType.CAFE,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = contents,
    date = datetime,
)

internal fun ModelImage.toEntity(): Document = Document(
    type = DocumentType.IMAGE,
    url = docUrl,
    thumbnail = thumbnailUrl,
    title = displaySiteName,
    content = collection,
    date = datetime,
)

internal fun ModelWeb.toEntity(): Document = Document(
    type = DocumentType.WEB,
    url = url,
    thumbnail = "",
    title = title,
    content = contents,
    date = datetime,
)

internal fun ModelBook.toEntity(): Document = Document(
    type = DocumentType.BOOK,
    url = url,
    thumbnail = thumbnail,
    title = title,
    content = "${authors.joinToString(",")}\n${translators.joinToString(",")}\n${publisher}\n$price",
    date = datetime,
)
