package com.apsl.surveyapp.core.auth

import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseAuthApiImpl @Inject constructor() : FirebaseAuthApi {

    private val firebaseAuth by lazy { Firebase.auth }

    private val scope = CoroutineScope(Job())

    override val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val authEvents: Flow<FirebaseAuthApi.Event> = callbackFlow {
        val listener = AuthStateListener { auth ->
            Timber.d("User ID: ${auth.currentUser?.uid}")
            launch {
                if (auth.currentUser != null) {
                    send(FirebaseAuthApi.Event.SignedIn)
                } else {
                    send(FirebaseAuthApi.Event.SignedOut)
                }
            }
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose { firebaseAuth.removeAuthStateListener(listener) }

    }.shareIn(scope = scope, started = SharingStarted.Lazily)

    override suspend fun createUser(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { continuation.resume(it?.user) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signIn(email: String, password: String) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
