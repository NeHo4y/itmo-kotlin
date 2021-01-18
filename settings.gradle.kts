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
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
                useModule("org.jetbrains.kotlin:kotlin-android-extensions:1.3.21")
                useModule("com.android.tools.build:gradle:3.3.1")
            }
        }
    }
}
rootProject.name = "feedbacKt"

enableFeaturePreview("GRADLE_METADATA")

include(":common")
include(":server")
include(":android")
include(":front")
