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

package io.github.beomjo.search.datasource.local

import androidx.paging.PagingSource
import io.github.beomjo.search.datasource.local.dao.BookmarkDao
import io.github.beomjo.search.datasource.local.table.BookmarkTable
import io.github.beomjo.search.datasource.remote.api.paging.BookmarkPagingSource
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.mapper.toEntity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class BookmarkPagingSourceSpec : BehaviorSpec() {

    private val bookmarkDao = mockk<BookmarkDao>()

    init {
        Given("LoadPram's key is not given") {
            val loadParam = mockk<PagingSource.LoadParams<Int>> {
                every { key } returns null
            }
            val bookmarkList = listOf<BookmarkTable>(mockk(relaxed = true))
            coEvery {
                bookmarkDao.getBookmarks(
                    BookmarkPagingSource.DEFAULT_OFFSET,
                    BookmarkPagingSource.LIMIT
                )
            } returns bookmarkList

            val pagingSource = BookmarkPagingSource(bookmarkDao)

            When("Call load") {
                val result = pagingSource.load(loadParam)

                Then("Should load the bookmark list from the database with the default key offset.") {
                    coVerify {
                        bookmarkDao.getBookmarks(
                            eq(BookmarkPagingSource.DEFAULT_OFFSET),
                            eq(BookmarkPagingSource.LIMIT)
                        )
                    }
                    result.shouldBeTypeOf<PagingSource.LoadResult.Page<Int, SearchDocument>>()
                    result.data shouldBe bookmarkList.map { it.toEntity() }
                    result.nextKey shouldBe null
                }
            }
        }

        Given("LoadPram's key is given") {
            val beforeOffset = 10
            val loadParam = mockk<PagingSource.LoadParams<Int>> {
                every { key } returns beforeOffset
            }
            val bookmarkList = (0 until 10).map { mockk<BookmarkTable>(relaxed = true) }.toList()
            coEvery {
                bookmarkDao.getBookmarks(
                    beforeOffset,
                    BookmarkPagingSource.LIMIT
                )
            } returns bookmarkList

            val pagingSource = BookmarkPagingSource(bookmarkDao)

            When("Call load") {
                val result = pagingSource.load(loadParam)

                Then("Should load the bookmark list from the database with offset.") {
                    coVerify {
                        bookmarkDao.getBookmarks(
                            eq(beforeOffset),
                            eq(BookmarkPagingSource.LIMIT)
                        )
                    }
                    result.shouldBeTypeOf<PagingSource.LoadResult.Page<Int, SearchDocument>>()
                    result.data shouldBe bookmarkList.map { it.toEntity() }
                    result.nextKey shouldBe beforeOffset + BookmarkPagingSource.LIMIT
                }
            }
        }

        Given("LoadPram's key is given and bookmarks error") {
            val beforeOffset = 10
            val loadParam = mockk<PagingSource.LoadParams<Int>> {
                every { key } returns beforeOffset
            }
            val exception = Exception()
            coEvery {
                bookmarkDao.getBookmarks(
                    beforeOffset,
                    BookmarkPagingSource.LIMIT
                )
            } throws exception

            val pagingSource = BookmarkPagingSource(bookmarkDao)

            When("Call load") {
                val result = pagingSource.load(loadParam)

                Then("LoadResult should be an error") {
                    coVerify {
                        bookmarkDao.getBookmarks(
                            eq(beforeOffset),
                            eq(BookmarkPagingSource.LIMIT)
                        )
                    }
                    result.shouldBeTypeOf<PagingSource.LoadResult.Error<Int, SearchDocument>>()
                    result.throwable shouldBe exception
                }
            }
        }
    }
}
