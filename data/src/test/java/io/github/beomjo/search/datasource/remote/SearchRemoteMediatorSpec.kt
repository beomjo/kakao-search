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

package io.github.beomjo.search.datasource.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.github.beomjo.search.data.R
import io.github.beomjo.search.datasource.local.AppDatabase
import io.github.beomjo.search.datasource.local.table.DocumentTable
import io.github.beomjo.search.datasource.local.table.RemoteKeyTable
import io.github.beomjo.search.datasource.remote.api.paging.SearchRemoteMediator
import io.github.beomjo.search.datasource.remote.api.service.DocumentsApi
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.Sort
import io.github.beomjo.search.entity.SortType
import io.github.beomjo.search.model.BlogResponse
import io.github.beomjo.search.model.CafeResponse
import io.github.beomjo.search.model.WebResponse
import io.github.beomjo.search.model.ImageResponse
import io.github.beomjo.search.model.BookResponse
import io.github.beomjo.search.model.DocumentListResponse
import io.github.beomjo.search.usecase.SearchPagingParam
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.mockk
import io.mockk.every
import io.mockk.slot
import io.mockk.just
import io.mockk.Runs
import io.mockk.unmockkAll
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.mockkStatic

@OptIn(ExperimentalPagingApi::class)
internal class SearchRemoteMediatorSpec : BehaviorSpec() {

    private val documentApi: DocumentsApi = mockk()

    private val database: AppDatabase = mockk(relaxUnitFun = true)

    private val state = mockk<PagingState<Int, DocumentTable>>()

    private val blogResponse = mockk<DocumentListResponse<BlogResponse>> {
        every { meta.isEnd } returns false
        every { documents } returns (0..SearchRemoteMediator.PER_PAGE_SIZE).map {
            mockk {
                every { url } returns "http://..."
                every { thumbnail } returns "http://.."
                every { title } returns "title"
                every { contents } returns "content"
                every { datetime } returns mockk()
            }
        }
    }
    private val cafeResponse = mockk<DocumentListResponse<CafeResponse>> {
        every { meta.isEnd } returns false
        every { documents } returns (0..SearchRemoteMediator.PER_PAGE_SIZE).map {
            mockk {
                every { url } returns "http://..."
                every { thumbnail } returns "http://.."
                every { title } returns "title"
                every { contents } returns "content"
                every { datetime } returns mockk()
            }
        }
    }
    private val webResponse = mockk<DocumentListResponse<WebResponse>> {
        every { meta.isEnd } returns false
        every { documents } returns (0..SearchRemoteMediator.PER_PAGE_SIZE).map {
            mockk {
                every { url } returns "http://"
                every { title } returns "title"
                every { contents } returns "content"
                every { datetime } returns mockk()
            }
        }
    }

    private val imagesResponse = mockk<DocumentListResponse<ImageResponse>> {
        every { meta.isEnd } returns false
        every { documents } returns (0..SearchRemoteMediator.PER_PAGE_SIZE).map {
            mockk {
                every { docUrl } returns "http://.."
                every { thumbnailUrl } returns "http://.."
                every { displaySiteName } returns "displaySiteName"
                every { collection } returns " collection"
                every { datetime } returns mockk()
            }
        }
    }

    private val bookResponse = mockk<DocumentListResponse<BookResponse>> {
        every { meta.isEnd } returns false
        every { documents } returns (0..SearchRemoteMediator.PER_PAGE_SIZE).map {
            mockk {
                every { title } returns "title"
                every { url } returns "http://.."
                every { thumbnail } returns "http://.."
                every { authors } returns listOf("author")
                every { translators } returns listOf("translator")
                every { publisher } returns "publisher"
                every { price } returns 10000
                every { datetime } returns mockk()
            }
        }
    }

    private val tableKeySlot = slot<RemoteKeyTable>()

    init {
        beforeTest {
            mockkStatic("androidx.room.RoomDatabaseKt")

            val transactionLambdaSlot = slot<suspend () -> R>()
            coEvery { database.withTransaction(capture(transactionLambdaSlot)) } coAnswers {
                transactionLambdaSlot.captured.invoke()
            }

            coEvery {
                documentApi.fetchBlog(
                    query = any(),
                    sort = any(),
                    page = any(),
                    size = any(),
                )
            } returns blogResponse

            coEvery {
                documentApi.fetchCafe(
                    query = any(),
                    sort = any(),
                    page = any(),
                    size = any(),
                )
            } returns cafeResponse

            coEvery {
                documentApi.fetchWeb(
                    query = any(),
                    sort = any(),
                    page = any(),
                    size = any(),
                )
            } returns webResponse

            coEvery {
                documentApi.fetchImages(
                    query = any(),
                    sort = any(),
                    page = any(),
                    size = any(),
                )
            } returns imagesResponse

            coEvery {
                documentApi.fetchBook(
                    query = any(),
                    sort = any(),
                    page = any(),
                    size = any(),
                )
            } returns bookResponse

            coEvery {
                database.documentDao().clearAllDocuments()
                database.remoteKeyDao().clearRemoteKeys()
                database.documentDao().insertDocuments(any())
                database.remoteKeyDao().insertKey(capture(tableKeySlot))
            } just Runs
        }

        Given("DocumentType.BLOG") {
            val param = SearchPagingParam(
                DocumentType.BLOG,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val state = mockk<PagingState<Int, DocumentTable>>()

            val pageSize = SearchRemoteMediator.PER_PAGE_SIZE

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchBlog(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(pageSize),
                        )
                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {
                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                            documentApi.fetchBlog(
                                query = any(),
                                sort = any(),
                                page = any(),
                                size = any(),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            documentApi.fetchBlog(
                                query = eq(param.query),
                                sort = eq(param.sort.value),
                                page = any(),
                                size = eq(pageSize),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        Given("DocumentType.CAFE") {
            val param = SearchPagingParam(
                DocumentType.CAFE,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val pageSize = SearchRemoteMediator.PER_PAGE_SIZE

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchCafe(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(pageSize),
                        )
                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {

                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {

                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                            documentApi.fetchCafe(
                                query = any(),
                                sort = any(),
                                page = any(),
                                size = any(),
                            )
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            documentApi.fetchCafe(
                                query = eq(param.query),
                                sort = eq(param.sort.value),
                                page = any(),
                                size = eq(pageSize),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        Given("DocumentType.WEB") {
            val param = SearchPagingParam(
                DocumentType.WEB,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val pageSize = SearchRemoteMediator.PER_PAGE_SIZE

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchWeb(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(pageSize),
                        )
                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {

                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {

                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                            documentApi.fetchWeb(
                                query = any(),
                                sort = any(),
                                page = any(),
                                size = any(),
                            )
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            documentApi.fetchWeb(
                                query = eq(param.query),
                                sort = eq(param.sort.value),
                                page = any(),
                                size = eq(pageSize),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        Given("DocumentType.IMAGE") {
            val param = SearchPagingParam(
                DocumentType.IMAGE,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val pageSize = SearchRemoteMediator.PER_PAGE_SIZE

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchImages(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(pageSize),
                        )
                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {

                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {

                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                            documentApi.fetchImages(
                                query = any(),
                                sort = any(),
                                page = any(),
                                size = any(),
                            )
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            documentApi.fetchImages(
                                query = eq(param.query),
                                sort = eq(param.sort.value),
                                page = any(),
                                size = eq(pageSize),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        Given("DocumentType.BOOK") {
            val param = SearchPagingParam(
                DocumentType.BOOK,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val pageSize = SearchRemoteMediator.PER_PAGE_SIZE

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchBook(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(pageSize),
                        )
                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {

                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {

                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                            documentApi.fetchBook(
                                query = any(),
                                sort = any(),
                                page = any(),
                                size = any(),
                            )
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            documentApi.fetchBook(
                                query = eq(param.query),
                                sort = eq(param.sort.value),
                                page = any(),
                                size = eq(pageSize),
                            )
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        Given("DocumentType.ALL") {
            val param = SearchPagingParam(
                DocumentType.ALL,
                SortType.TITLE,
                query = "IU",
                sort = Sort.ACCURACY,
            )

            val state = mockk<PagingState<Int, DocumentTable>>()

            val allTypePageSize =
                SearchRemoteMediator.PER_PAGE_SIZE / SearchRemoteMediator.DOCUMENT_TYPE_NUM

            val remoteMediator = SearchRemoteMediator(
                requestParam = param,
                documentApi = documentApi,
                database = database
            )

            When("Call load, LoadType is REFRESH") {
                val mediatorResult = remoteMediator.load(
                    LoadType.REFRESH,
                    state
                )

                Then("MediatorResult Success") {
                    coVerifyOrder {
                        database.documentDao().clearAllDocuments()
                        database.remoteKeyDao().clearRemoteKeys()
                        documentApi.fetchBlog(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(allTypePageSize),
                        )
                        documentApi.fetchCafe(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(allTypePageSize),
                        )
                        documentApi.fetchWeb(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(allTypePageSize),
                        )
                        documentApi.fetchImages(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(allTypePageSize),
                        )
                        documentApi.fetchBook(
                            query = eq(param.query),
                            sort = eq(param.sort.value),
                            page = any(),
                            size = eq(allTypePageSize),
                        )

                        database.documentDao().insertDocuments(any())
                        database.remoteKeyDao().insertKey(any())
                    }

                    tableKeySlot.captured.position shouldBe 1
                    tableKeySlot.captured.prevKey shouldBe null
                    tableKeySlot.captured.nextKey shouldBe 2

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe false
                }
            }

            When("Call load, LoadType is PREPEND") {
                val mediatorResult = remoteMediator.load(
                    LoadType.PREPEND,
                    state
                )

                Then("MediatorResult Success And endOfPaginationReached is true") {
                    coVerify {
                        database wasNot Called
                        documentApi wasNot Called
                        database.documentDao() wasNot Called
                        database.remoteKeyDao() wasNot Called
                    }

                    mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                    mediatorResult.endOfPaginationReached shouldBe true
                }
            }

            When("Call load, LoadType is APPEND") {

                And("getRemoteKeyTable is null") {
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns null

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {

                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                        }

                        coVerify(inverse = true) {
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }

                And("getRemoteKeyTable is not null") {
                    val remoteKeyTable = arrayOf(mockk<RemoteKeyTable> {
                        every { position } returns 1
                        every { prevKey } returns null
                        every { nextKey } returns 2
                    })
                    coEvery { database.remoteKeyDao().getRemoteKeyTable() } returns remoteKeyTable

                    val mediatorResult = remoteMediator.load(
                        LoadType.APPEND,
                        state
                    )

                    Then("MediatorResult.Success, endOfPaginationReached = false") {
                        coVerifyOrder {
                            database.remoteKeyDao().getRemoteKeyTable()
                            database.documentDao().insertDocuments(any())
                            database.remoteKeyDao().insertKey(any())
                        }
                        coVerify(inverse = true) {
                            database.documentDao().clearAllDocuments()
                            database.remoteKeyDao().clearRemoteKeys()
                        }

                        tableKeySlot.captured.position shouldBe 2
                        tableKeySlot.captured.prevKey shouldBe 1
                        tableKeySlot.captured.nextKey shouldBe 3

                        mediatorResult.shouldBeTypeOf<RemoteMediator.MediatorResult.Success>()
                        mediatorResult.endOfPaginationReached shouldBe false
                    }
                }
            }
        }

        afterTest {
            unmockkAll()
        }
    }
}
