rootProject.name = "KotlinMultiplatformConfiguration"


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":shared")
include(":android")
include(":web")
include(":desktop")
