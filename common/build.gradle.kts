val INCLUDE_ANDROID: String by extra

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx/")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js {
        browser {
            binaries.executable()
        }
        useCommonJs()
    }

    sourceSets {
        val ktorVersion = "1.5.0"
        fun ktorClient(module: String, version: String = ktorVersion) = "io.ktor:ktor-client-$module:$version"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation(ktorClient("core"))
                implementation(ktorClient("json"))
                implementation(ktorClient("serialization"))
            }
        }

        val jvmMain by getting

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains:kotlin-react:17.0.1-pre.137-kotlin-1.4.21")
                implementation("org.jetbrains:kotlin-react-redux:7.2.1-pre.136-kotlin-1.4.21")
                implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.137-kotlin-1.4.21")
                implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.136-kotlin-1.4.21")
                implementation("org.jetbrains:kotlin-redux:4.0.5-pre.136-kotlin-1.4.21")

                implementation(npm("history", "4.10.1"))
                implementation(npm("connected-react-router", "5.0.1"))
                implementation(npm("markdown-it", "10.0.0"))
                implementation(npm("sanitize-html", "1.23.0"))
            }
        }
    }
}
