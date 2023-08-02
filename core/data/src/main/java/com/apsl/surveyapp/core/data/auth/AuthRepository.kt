package com.apsl.surveyapp.core.data.auth

import com.apsl.surveyapp.core.models.Email
import com.apsl.surveyapp.core.models.Password

interface AuthRepository {
    val isUserSignedIn: Boolean
    suspend fun signIn(email: Email, password: Password)
    fun signOut()
}
