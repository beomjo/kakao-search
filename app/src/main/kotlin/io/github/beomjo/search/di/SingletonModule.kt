package io.github.beomjo.search.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.beomjo.search.ApiKey
import io.github.beomjo.search.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
internal object SingletonModule {

    @Provides
    @ApiKey
    fun provideApiKey(): String {
        return BuildConfig.REST_KEY
    }
}
