import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    android
    `kotlin-android`
    `kotlin-kapt`
    `detekt-setting`
    id("androidx.navigation.safeargs.kotlin")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()

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

    implementation(Dependency.Kotlin.SDK)
    implementation(Dependency.Kotlin.COROUTINE_CORE)
    implementation(Dependency.Kotlin.COROUTINE_ANDROID)

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

    testImplementation(TestDependency.JUNIT)
    testImplementation(TestDependency.MOCKK)
    testImplementation(TestDependency.COROUTINE_TEST)
    androidTestImplementation(AndroidTestDependency.TEST_RUNNER)
    androidTestImplementation(AndroidTestDependency.ESPRESSO_CORE)
}
