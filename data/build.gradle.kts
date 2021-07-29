plugins {
    `android-library`
    `detekt-setting`
}

android {
    compileSdkVersion(AndroidEnv.ANDROID_COMPILE)

    defaultConfig {
        minSdkVersion(AndroidEnv.ANDROID_MIN)
        targetSdkVersion(AndroidEnv.ANDROID_TARGET)
    }
}


dependencies {
    implementation(project(":domain"))
}
