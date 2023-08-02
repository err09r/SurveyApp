plugins {
    id("com.apsl.surveyapp.core")
}

dependencies {
    implementation(project(":core:auth"))
    implementation(project(":core:models"))
    implementation(project(":core:realtime"))
    implementation(project(":core:storage"))
    implementation(project(":core:util:android"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
