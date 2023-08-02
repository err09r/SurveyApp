package com.apsl.surveyapp

import androidx.lifecycle.ViewModel
import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(firebaseAuthApi: FirebaseAuthApi) : ViewModel() {
    val authEvents = firebaseAuthApi.authEvents
}
