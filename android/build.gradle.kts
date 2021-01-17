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

    defaultConfig {
        applicationId = "com.github.neho4u"
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 21
        versionName = "2.0.0"
        multiDexEnabled = true
    }
}


repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation("com.android.support:multidex:1.0.3")
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support:customtabs:28.0.0")
    implementation("com.android.support:cardview-v7:28.0.0")
    implementation("com.android.support:recyclerview-v7:28.0.0")
    implementation("com.android.support:support-v4:28.0.0")
    implementation("com.android.support:design:28.0.0")
    implementation("com.android.support.constraint:constraint-layout:2.0.0-alpha3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21")
    implementation("com.github.kittinunf.fuel:fuel-android:1.13.0")
    implementation("com.google.code.gson:gson:2.8.2")
    implementation("org.jetbrains.anko:anko-commons:0.10.0")
}
