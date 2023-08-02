package com.apsl.surveyapp.core.storage

interface FirebaseStorageApi {
    suspend fun uploadFile(uri: String, folderName: String): String
}
