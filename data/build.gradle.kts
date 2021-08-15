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

plugins {
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
    `detekt-setting`
}

android {
    compileSdk = AndroidEnv.ANDROID_COMPILE

    defaultConfig {
        minSdk = AndroidEnv.ANDROID_MIN
        targetSdk = AndroidEnv.ANDROID_TARGET
    }
}


dependencies {
    implementation(project(":domain"))

    implementation(Dependency.Kotlin.COROUTINE_CORE)
    implementation(Dependency.Kotlin.COROUTINE_ANDROID)

    implementation(Dependency.Paging3.RUNTIME)

    implementation(Dependency.Hilt.CORE)
    kapt(Dependency.Hilt.APT)

    implementation(Dependency.Google.GSON)

    implementation(Dependency.Retrofit.CORE)
    implementation(Dependency.Retrofit.CONVERT_GSON)

    implementation(Dependency.OkHttp.CORE)
    implementation(Dependency.OkHttp.CONNECTION)
    implementation(Dependency.OkHttp.LOGGING_INTERCEPTOR)

    implementation(Dependency.Room.RUNTIME)
    implementation(Dependency.Room.KTX)
    kapt(Dependency.Room.APT)
}
