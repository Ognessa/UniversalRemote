rootProject.name = "UniversalRemote"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core")

include(":feature-list-api")
project(":feature-list-api").projectDir = file("feature/list/api")

include(":feature-list-presentation")
project(":feature-list-presentation").projectDir = file("feature/list/presentation")

include(":feature-editor-api")
project(":feature-editor-api").projectDir = file("feature/editor/api")

include(":feature-editor-presentation")
project(":feature-editor-presentation").projectDir = file("feature/editor/presentation")

include(":feature-library-api")
project(":feature-library-api").projectDir = file("feature/library/api")

include(":feature-library-presentation")
project(":feature-library-presentation").projectDir = file("feature/library/presentation")

include(":feature-signal-api")
project(":feature-signal-api").projectDir = file("feature/signal/api")

include(":feature-signal-presentation")
project(":feature-signal-presentation").projectDir = file("feature/signal/presentation")
