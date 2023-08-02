package com.apsl.surveyapp.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.apsl.surveyapp.feature.auth.signup.SignUpScreen
import com.apsl.surveyapp.feature.auth.signin.SignInScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "signin", route = "authGraph") {
        composable(route = "signin") {
            SignInScreen(onNavigateToSignUp = { navController.navigate("signup") })
        }
        composable(route = "signup") {
            SignUpScreen()
        }
    }
}
