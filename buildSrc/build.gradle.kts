plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

object PluginVersion {
    const val GRADLE = "4.2.2"
    const val KOTLIN = "1.5.21"
    const val DETEKT = "1.17.1"
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginVersion.GRADLE}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.KOTLIN}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${PluginVersion.DETEKT}")
}
