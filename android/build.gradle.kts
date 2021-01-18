plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    val ktorVersion = "1.5.0"
    fun ktorClient(module: String, version: String = ktorVersion) = "io.ktor:ktor-client-$module:$version"

    implementation(project(":common"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation(ktorClient("core-jvm"))
    implementation(ktorClient("json-jvm"))
    implementation(ktorClient("serialization-jvm"))
    implementation(ktorClient("okhttp"))
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.github.neho4u"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
