plugins {
    id("com.apsl.surveyapp.core")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:auth"))
    implementation(project(":core:data"))
    implementation(project(":core:realtime"))
    implementation(project(":core:storage"))
    implementation(project(":core:util:android"))

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
}
