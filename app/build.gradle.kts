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

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    android
    `kotlin-android`
    `kotlin-kapt`
    `detekt-setting`
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

val version: Version.Property = Version.getVersionProperty()
val restKey: String? = gradleLocalProperties(rootDir).getProperty(REST_KEY)

android {
    compileSdk = AndroidEnv.ANDROID_COMPILE
    buildToolsVersion = AndroidEnv.BUILD_TOOL

    defaultConfig {
        applicationId = AndroidEnv.APPLICATION_ID
        minSdk = AndroidEnv.ANDROID_MIN
        targetSdk = AndroidEnv.ANDROID_TARGET
        vectorDrawables.useSupportLibrary = true
        versionCode = version.code
        versionName = version.name

        testInstrumentationRunner = TestDependency.ANDROID_JUNIT_RUNNER

        buildConfigField("String", REST_KEY, "\"$restKey\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.testLogging {
                events("passed", "skipped", "failed")
                it.outputs.upToDateWhen {
                    false
                }
                showStandardStreams = true
                showCauses = true
                showExceptions = true
                showStackTraces = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    kapt {
        javacOptions {
            option("-Adagger.fastInit=ENABLED")
            option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":android-compilation"))

    implementation(Dependency.Kotlin.SDK)
    implementation(Dependency.Kotlin.COROUTINE_CORE)
    implementation(Dependency.Kotlin.COROUTINE_ANDROID)
    implementation(Dependency.Kotlin.REFLECTION)

    implementation(Dependency.AndroidX.APP_COMPAT)
    implementation(Dependency.AndroidX.MATERIAL)
    implementation(Dependency.AndroidX.APP_COMPAT)
    implementation(Dependency.AndroidX.CONSTRAINT_LAYOUT)

    implementation(Dependency.KTX.CORE)
    implementation(Dependency.KTX.LIFECYCLE_LIVEDATA)
    implementation(Dependency.KTX.LIFECYCLE_VIEWMODEL)
    implementation(Dependency.KTX.ACTIVITY)
    implementation(Dependency.KTX.FRAGMENT)

    implementation(Dependency.Google.MATERIAL)
    implementation(Dependency.Google.GSON)

    implementation(Dependency.Glide.CORE)
    kapt(Dependency.Glide.APT)

    implementation(Dependency.Hilt.CORE)
    kapt(Dependency.Hilt.APT)

    implementation(Dependency.Room.RUNTIME)
    kapt(Dependency.Room.APT)

    implementation(Dependency.Paging3.RUNTIME)

    implementation(Dependency.Navigation.FRAGMENT)
    implementation(Dependency.Navigation.UI)

    implementation(Dependency.BINDABLES)

    testImplementation(TestDependency.MOCKK)
    testImplementation(TestDependency.COROUTINE_TEST)
    testImplementation(TestDependency.KOTEST)
    androidTestImplementation(AndroidTestDependency.TEST_RUNNER)
    androidTestImplementation(AndroidTestDependency.ESPRESSO_CORE)
}
