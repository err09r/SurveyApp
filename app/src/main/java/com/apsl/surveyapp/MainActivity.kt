package com.apsl.surveyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurveyAppTheme {
                val navHostController = rememberNavController()

                LaunchedEffect(mainActivityViewModel, navHostController) {
                    mainActivityViewModel.authEvents.collect { event ->
                        when (event) {
                            is FirebaseAuthApi.Event.SignedIn -> {
                                Timber.d("Signed In")
                                navHostController.navigate("surveyGraph") {
                                    popUpTo("authGraph") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }

                            is FirebaseAuthApi.Event.SignedOut -> {
                                Timber.d("Signed Out")
                                navHostController.navigate("authGraph") {
                                    popUpTo("surveyGraph") { inclusive = true }
                                }
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Navigation(
                        navHostController = navHostController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}
