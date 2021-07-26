plugins {
    android
    `kotlin-android`
}

val version = Project.Version.value

android {
    compileSdkVersion(Project.Config.ANDROID_COMPILE)
    buildToolsVersion = Project.Config.BUILD_TOOL

    defaultConfig {
        applicationId = "io.beomjo.kakao.search"
        minSdkVersion(Project.Config.ANDROID_MIN)
        targetSdkVersion(Project.Config.ANDROID_TARGET)
        vectorDrawables.useSupportLibrary = true
        versionCode = version.code
        versionName = version.name

        testInstrumentationRunner = Dependency.Test.ANDROID_JUNIT_RUNNER
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Dependency.Kotlin.SDK)
    implementation(Dependency.KTX.CORE)
    implementation(Dependency.AndroidX.APP_COMPAT)
    implementation(Dependency.AndroidX.MATERIAL)
    implementation(Dependency.AndroidX.CONSTRAINT_LAYOUT)

    testImplementation(Dependency.Test.JUNIT)

    androidTestImplementation(Dependency.AndroidTest.TEST_RUNNER)
    androidTestImplementation(Dependency.AndroidTest.ESPRESSO_CORE)
}
