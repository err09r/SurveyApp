@file:Suppress("UnstableApiUsage")

plugins {
    id("com.apsl.surveyapp.core")
}

android {
    buildTypes.configureEach {
        buildConfigField(
            type = "String",
            name = "REALTIME_DB_URL",
            value = "\"https://survey-app-ec866-default-rtdb.europe-west1.firebasedatabase.app/\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database.ktx)
    implementation(libs.javax.inject)
}
