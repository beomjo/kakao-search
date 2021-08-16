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

package io.github.beomjo.search.datasource.remote.api

import com.google.gson.GsonBuilder
import io.github.beomjo.search.data.BuildConfig
import io.github.beomjo.search.datasource.remote.api.converter.DateConverter
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.Date
import java.util.concurrent.TimeUnit

internal object RetrofitAdapter {
    private const val TIMEOUT: Long = 10

    fun getInstance(baseUrl: String, apiKey: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(apiKey))
            .addConverterFactory(getGsonConvertFactory())
            .build()
    }

    private fun getOkHttpClient(apiKey: String): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(getCookieManager()))
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor { getRequestInterceptor(it, apiKey) }
            .build()
    }

    private fun getGsonConvertFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateConverter())
            .create()
        return GsonConverterFactory.create(gson)
    }

    private fun getCookieManager(): CookieManager {
        return CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private fun getRequestInterceptor(chain: Interceptor.Chain, apiKey: String): Response {
        return chain.request().let { originRequest ->
            chain.proceed(
                originRequest.newBuilder()
                    .addHeader("Authorization", "KakaoAK $apiKey")
                    .build()
            )
        }
    }
}
