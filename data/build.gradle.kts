plugins {
    `android-library`
    `detekt-setting`
}

android {
    compileSdkVersion(AndroidEnv.Config.ANDROID_COMPILE)

    defaultConfig {
        minSdkVersion(AndroidEnv.Config.ANDROID_MIN)
        targetSdkVersion(AndroidEnv.Config.ANDROID_TARGET)
    }
}


dependencies {
    implementation(project(":domain"))
}
