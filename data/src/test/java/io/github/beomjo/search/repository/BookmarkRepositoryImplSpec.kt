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

package io.github.beomjo.search.repository

import androidx.paging.PagingData
import io.github.beomjo.search.datasource.local.dao.BookmarkDao
import io.github.beomjo.search.datasource.local.table.BookmarkTable
import io.github.beomjo.search.datasource.remote.api.paging.BookmarkPagingSource
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.mapper.toBookmarkTable
import io.github.beomjo.search.mapper.toEntity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class BookmarkRepositoryImplSpec : BehaviorSpec() {

    private val bookmarkDao = mockk<BookmarkDao>(relaxed = true)

    private val bookmarkPagingSource = mockk<BookmarkPagingSource>(relaxed = true)

    init {
        Given("Given a SearchDocument for insertBookmark") {
            val searchDocument = SearchDocument(
                type = DocumentType.ALL,
                url = "http://",
                thumbnail = "http://..",
                title = "title",
                content = "content",
                date = mockk()
            )

            val repositoryImpl = BookmarkRepositoryImpl(bookmarkDao, bookmarkPagingSource)

            val slot = slot<BookmarkTable>()
            coEvery { bookmarkDao.insertBookmark(capture(slot)) } just Runs

            When("insert bookmark") {
                repositoryImpl.setBookmark(searchDocument)

                Then("Should convert SearchDocument to table and store it in db") {
                    coVerify { bookmarkDao.insertBookmark(any<BookmarkTable>()) }
                    searchDocument.toBookmarkTable().url shouldBe slot.captured.url
                    searchDocument.toBookmarkTable().type shouldBe slot.captured.type
                    searchDocument.toBookmarkTable().thumbnail shouldBe slot.captured.thumbnail
                    searchDocument.toBookmarkTable().title shouldBe slot.captured.title
                    searchDocument.toBookmarkTable().content shouldBe slot.captured.content
                    searchDocument.toBookmarkTable().writeDate shouldBe slot.captured.writeDate
                }
            }
        }

        Given("Given a SearchDocument for deleteBookmark") {
            val searchDocument = SearchDocument(
                type = DocumentType.ALL,
                url = "http://",
                thumbnail = "http://..",
                title = "title",
                content = "content",
                date = mockk()
            )

            val repositoryImpl = BookmarkRepositoryImpl(bookmarkDao, bookmarkPagingSource)

            When("delete bookmark") {
                repositoryImpl.removeBookmark(searchDocument)

                Then("Should delete SearchDocumentTable from db") {
                    coVerify { bookmarkDao.deleteBookmark(eq(searchDocument.url)) }
                }
            }
        }

        Given("Given a SearchDocument for isBookmark") {
            val searchDocument = SearchDocument(
                type = DocumentType.ALL,
                url = "http://",
                thumbnail = "http://..",
                title = "title",
                content = "content",
                date = mockk()
            )

            val repositoryImpl = BookmarkRepositoryImpl(bookmarkDao, bookmarkPagingSource)

            When("delete bookmark") {

                And("It is saved as a bookmark in the DB") {
                    every { bookmarkDao.isBookmarked(searchDocument.url) } returns flowOf(
                        searchDocument.toBookmarkTable()
                    )

                    val result = repositoryImpl.isBookmarked(searchDocument)

                    Then("Should return bookmarked") {
                        verify { bookmarkDao.isBookmarked(eq(searchDocument.url)) }
                        result.first() shouldBe true
                    }
                }

                And("It is not saved as a bookmark in the DB") {
                    every { bookmarkDao.isBookmarked(searchDocument.url) } returns flowOf(null)

                    val result = repositoryImpl.isBookmarked(searchDocument)

                    Then("Should return bookmarked") {
                        verify { bookmarkDao.isBookmarked(eq(searchDocument.url)) }
                        result.first() shouldBe false
                    }
                }
            }
        }

        Given("Given a nothing") {
            val repositoryImpl = BookmarkRepositoryImpl(bookmarkDao, bookmarkPagingSource)

            When("Call getBookmarkPagingData") {
                val result = repositoryImpl.getBookmarkPagingData()

                Then("Should return PagingData flow") {
                    result.first().shouldBeTypeOf<PagingData<SearchDocument>>()
                }
            }
        }


        Given("Given a nothing") {
            val bookmarkTable = listOf(
                BookmarkTable(
                    type = DocumentType.BOOK,
                    url = "http://",
                    title = "fqf",
                    content = "fafa",
                    writeDate = mockk(),
                    bookmarkedDate = mockk(),
                    thumbnail = "http://"
                )
            )
            val repositoryImpl = BookmarkRepositoryImpl(bookmarkDao, bookmarkPagingSource)

            every { bookmarkDao.getBookmarks() } returns flowOf(bookmarkTable)

            When("Call getBookmarkList") {
                val result = repositoryImpl.getBookmarkList()

                Then("Should return flow") {
                    result.first() shouldBe bookmarkTable.map { it.toEntity() }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}