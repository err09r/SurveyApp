package com.apsl.surveyapp.core.data.user

import com.apsl.surveyapp.core.models.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDataModel(): User {
    return User(
        id = this.uid,
        email = this.email,
        displayName = this.displayName
    )
}
