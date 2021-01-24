plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.3"
    defaultConfig {
        applicationId = "com.github.neho4u"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 21
        versionName = "2.0.0"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.21")
    implementation("io.noties.markwon:core:4.6.1")
    implementation("androidx.preference:preference:1.1.1")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")
}
