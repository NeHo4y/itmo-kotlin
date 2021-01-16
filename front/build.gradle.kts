plugins {
    kotlin("js")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

dependencies {
    val ktorVersion = "1.5.0"
    fun ktorClient(module: String, version: String = ktorVersion) = "io.ktor:ktor-client-$module:$version"

    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
    implementation("org.jetbrains:kotlin-react:17.0.1-pre.136-kotlin-1.4.10")
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.10")
    implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.136-kotlin-1.4.10")
    implementation("org.jetbrains:kotlin-react-redux:7.2.1-pre.136-kotlin-1.4.10")
    implementation("org.jetbrains:kotlin-redux:4.0.5-pre.136-kotlin-1.4.10")

    implementation(ktorClient("core-js"))
    implementation(ktorClient("json-js"))
    implementation(ktorClient("serialization-js"))
}

kotlin {
    js {
        browser {
            binaries.executable()
        }
    }
}
