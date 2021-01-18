plugins {
    kotlin("js")
}

group = "com.github.neho4u"
version = "1.0-SNAPSHOT"

val kotlinVersion: String by extra
val reactVersion: String by extra
val extensionsVersion: String by extra
val reactRouterDomVersion: String by extra
val reactReduxVersion: String by extra
val reduxVersion: String by extra
val ktorClientVersion: String by extra

repositories {
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven("https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    fun ktorClient(module: String, version: String = ktorClientVersion) = "io.ktor:ktor-client-$module:$version"
    fun jsWrapper(module: String, version: String) = "org.jetbrains:kotlin-$module:$version-kotlin-$kotlinVersion"

    implementation(project(":common"))
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
    implementation(jsWrapper("extensions", extensionsVersion))
    implementation(jsWrapper("react", reactVersion))
    implementation(jsWrapper("react-dom", reactVersion))
    implementation(jsWrapper("react-router-dom", reactRouterDomVersion))
    implementation(jsWrapper("react-redux", reactReduxVersion))
    implementation(jsWrapper("redux", reduxVersion))

    implementation(npm("history", "4.10.1"))
    implementation(npm("connected-react-router", "6.8.0"))
    implementation(npm("react", reactVersion.split("-").first()))
    implementation(npm("react-dom", reactVersion.split("-").first()))
    implementation(npm("redux", reduxVersion.split("-").first()))
    implementation(npm("react-redux", reactReduxVersion.split("-").first()))
    implementation(npm("core-js", "3.8.2"))
    implementation(npm("markdown-it", "10.0.0"))
    implementation(npm("sanitize-html", "1.23.0"))

    implementation(ktorClient("core-js"))
    implementation(ktorClient("json-js"))
    implementation(ktorClient("serialization-js"))
}

kotlin {
    js {
        browser {
            binaries.executable()
        }
        useCommonJs()
    }
}
