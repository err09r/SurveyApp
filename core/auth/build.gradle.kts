plugins {
    id("com.apsl.surveyapp.core")
}

dependencies {
    implementation(project(":core:models"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
