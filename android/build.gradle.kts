plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

android {
    compileSdkVersion(28)
    buildToolsVersion = "30.0.3"
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.github.neho4u"
        minSdkVersion(24)
        targetSdkVersion(28)
        versionCode = 21
        versionName = "2.0.0"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    lintOptions {
        isAbortOnError = false
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    lintOptions {
        isAbortOnError = false
    }
}

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation(ktorClient("core-jvm"))
    implementation(ktorClient("json-jvm"))
    implementation(ktorClient("serialization-jvm"))
    implementation(ktorClient("okhttp"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")

    implementation("com.android.support:multidex:1.0.3")
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support:customtabs:28.0.0")
    implementation("com.android.support:cardview-v7:28.0.0")
    implementation("com.android.support:recyclerview-v7:28.0.0")
    implementation("com.android.support:support-v4:28.0.0")
    implementation("com.android.support:design:28.0.0")
    implementation("com.android.support.constraint:constraint-layout:2.0.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.21")
}
