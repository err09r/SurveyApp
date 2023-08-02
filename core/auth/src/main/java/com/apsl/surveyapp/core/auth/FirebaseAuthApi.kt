package com.apsl.surveyapp.core.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthApi {
    val isUserSignedIn: Boolean
    val currentUser: FirebaseUser?
    val authEvents: Flow<Event>
    suspend fun createUser(email: String, password: String): FirebaseUser?
    suspend fun signIn(email: String, password: String)
    fun signOut()

    sealed interface Event {
        data object SignedIn : Event
        data object SignedOut : Event
    }
}
