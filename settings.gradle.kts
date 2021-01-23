val INCLUDE_ANDROID: String by extra

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
}
rootProject.name = "feedbacKt"

enableFeaturePreview("GRADLE_METADATA")

include(":common")
include(":server")
include(":front")
