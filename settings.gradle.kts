@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "SurveyApp"
include(":app")
include(":core:auth")
include(":core:data")
include(":core:di")
include(":core:models")
include(":core:realtime")
include(":core:storage")
include(":core:ui")
include(":core:util:android")
include(":core:util:kotlin")
include(":feature:auth")
include(":feature:survey")
