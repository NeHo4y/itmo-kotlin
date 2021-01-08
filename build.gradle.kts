buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}

group = "com.github.neho4u"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        version.set("0.39.0")
        enableExperimentalRules.set(true)
        verbose.set(true)
        outputToConsole.set(true)
    }
}
