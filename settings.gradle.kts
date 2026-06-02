pluginManagement {
    repositories {
        maven("https://repo.faststats.dev/releases")
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "tweaks"