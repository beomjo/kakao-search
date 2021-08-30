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

package io.github.beomjo.search

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.beomjo.search.datasource.local.AppDatabase
import io.github.beomjo.search.datasource.local.dao.DocumentDao
import io.github.beomjo.search.datasource.local.dao.RemoteKeyDao
import io.github.beomjo.search.datasource.local.dao.SearchHistoryDao
import io.github.beomjo.search.datasource.remote.api.RetrofitAdapter
import io.github.beomjo.search.datasource.remote.api.Urls
import io.github.beomjo.search.datasource.remote.api.service.DocumentsApi
import io.github.beomjo.search.repository.SearchRepository
import io.github.beomjo.search.repository.SearchRepositoryImpl
import javax.inject.Singleton

@Module(includes = [DataModule.DataBindModule::class])
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Singleton
    @Provides
    fun provideDocumentApi(
        @ApiKey apiKey: String
    ): DocumentsApi {
        return RetrofitAdapter.getInstance(Urls.baseUrl, apiKey)
            .create(DocumentsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDocumentDao(database: AppDatabase): DocumentDao {
        return database.documentDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(database: AppDatabase): RemoteKeyDao {
        return database.remoteKeyDao()
    }

    @Singleton
    @Provides
    fun provideSearchHistoryDao(database: AppDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DataBindModule {
        @Binds
        abstract fun provideDocumentRepository(repository: SearchRepositoryImpl): SearchRepository
    }
}
