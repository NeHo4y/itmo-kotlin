val INCLUDE_ANDROID: String by extra

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
                useModule("org.jetbrains.kotlin:kotlin-android-extensions:1.4.21")
                useModule("com.android.tools.build:gradle:4.1.2")
            }
        }
    }
}
rootProject.name = "feedbacKt"

enableFeaturePreview("GRADLE_METADATA")

if (INCLUDE_ANDROID == "true") {
    include( ":android")
}
include(":common")
include(":server")
include(":front")
