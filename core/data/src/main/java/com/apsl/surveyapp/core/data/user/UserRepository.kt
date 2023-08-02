package com.apsl.surveyapp.core.data.user

import com.apsl.surveyapp.core.models.Email
import com.apsl.surveyapp.core.models.Password
import com.apsl.surveyapp.core.models.User

interface UserRepository {
    val currentUser: User?
    suspend fun createUser(email: Email, password: Password): User?
}
