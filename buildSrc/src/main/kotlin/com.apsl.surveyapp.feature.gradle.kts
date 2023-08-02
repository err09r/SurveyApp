@file:Suppress("UnstableApiUsage")

plugins {
    id("com.apsl.surveyapp.android-library")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "${Config.namespace}.feature.${project.name}"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:models"))
    implementation(project(":core:ui"))
    implementation(project(":core:util:android"))
    implementation(project(":core:util:kotlin"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.toolingPreview)
    implementation(libs.androidx.compose.ui.util)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.hilt.navigationCompose)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
}
