plugins {
    id("com.apsl.surveyapp.core")
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage.ktx)
    implementation(libs.javax.inject)
}
