package com.apsl.surveyapp.core.storage

import androidx.core.net.toUri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseStorageApiImpl @Inject constructor() : FirebaseStorageApi {

    private val firebaseStorage by lazy { Firebase.storage }

    override suspend fun uploadFile(uri: String, folderName: String) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->

            val successListener: (UploadTask.TaskSnapshot?) -> Unit = { task ->
                val downloadUrlTask = task?.metadata?.reference?.downloadUrl

                if (downloadUrlTask == null) {
                    continuation.resumeWithException(Exception("Download URL task is null"))
                } else {
                    downloadUrlTask
                        .addOnSuccessListener { downloadUrl ->
                            continuation.resume(downloadUrl.toString())
                        }
                        .addOnFailureListener {
                            continuation.resumeWithException(Exception("Download URL is null"))
                        }
                }
            }

            val failureListener: (Exception) -> Unit = { exception ->
                continuation.resumeWithException(exception)
            }

            val path = "$folderName/${generateFileName()}"
            val storageTask = firebaseStorage.reference.child(path).putFile(uri.toUri())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)

            continuation.invokeOnCancellation {
                storageTask.removeOnSuccessListener(successListener)
                storageTask.removeOnFailureListener(failureListener)
            }
        }
    }

    private fun generateFileName(): String {
        return UUID
            .randomUUID()
            .toString()
            .replaceRange(startIndex = 0, endIndex = 3, replacement = "IMG-")
    }
}
