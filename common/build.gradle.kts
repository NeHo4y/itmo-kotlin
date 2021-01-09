plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val jvmMain by getting
        val jsMain by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
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
