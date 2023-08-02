package com.apsl.surveyapp.core.data.user

import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import com.apsl.surveyapp.core.models.Email
import com.apsl.surveyapp.core.models.Password
import com.apsl.surveyapp.core.models.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuthApi: FirebaseAuthApi
) : UserRepository {

    override val currentUser: User?
        get() = firebaseAuthApi.currentUser?.toDataModel()

    override suspend fun createUser(email: Email, password: Password): User? {
        return firebaseAuthApi.createUser(email.value, password.value)?.toDataModel()
    }
}
