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

package io.github.beomjo.search.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.beomjo.search.ApiKey
import io.github.beomjo.search.BuildConfig
import io.github.beomjo.search.util.DateHelper

@Module
@InstallIn(SingletonComponent::class)
internal object SingletonModule {

    @Provides
    @ApiKey
    fun provideApiKey(): String {
        return BuildConfig.REST_KEY
    }


    @Provides
    fun provideDateConverter(
        @ApplicationContext context: Context
    ): DateHelper {
        return DateHelper(context)
    }
}
