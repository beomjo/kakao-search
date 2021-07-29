plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

object PluginVersion {
    const val GRADLE = "4.2.2"
    const val KOTLIN = "1.5.21"
    const val DETEKT = "1.17.1"
    const val HILT = "2.38.1"
    const val DEPENDENCY_CHECKER = "0.39.0"
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginVersion.GRADLE}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.KOTLIN}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${PluginVersion.DETEKT}")
    implementation("com.google.dagger:hilt-android-gradle-plugin:${PluginVersion.HILT}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${PluginVersion.DEPENDENCY_CHECKER}")
}
