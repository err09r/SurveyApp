package com.apsl.surveyapp.core.data.auth

import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import com.apsl.surveyapp.core.models.Email
import com.apsl.surveyapp.core.models.Password
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthApi: FirebaseAuthApi
) : AuthRepository {

    override val isUserSignedIn: Boolean
        get() = firebaseAuthApi.isUserSignedIn

    override suspend fun signIn(email: Email, password: Password) {
        firebaseAuthApi.signIn(email = email.value, password = password.value)
    }

    override fun signOut() {
        firebaseAuthApi.signOut()
    }
}
