plugins {
    `android-library`
    `detekt-setting`
}

android {
    compileSdkVersion(Project.Config.ANDROID_COMPILE)

    defaultConfig {
        minSdkVersion(Project.Config.ANDROID_MIN)
        targetSdkVersion(Project.Config.ANDROID_TARGET)
    }
}


dependencies {
    implementation(project(":domain"))
}
