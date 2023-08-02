package com.apsl.surveyapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apsl.surveyapp.feature.auth.navigation.authGraph
import com.apsl.surveyapp.feature.survey.navigation.surveyGraph

@Composable
fun Navigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = "surveyGraph",
        modifier = modifier,
        route = "rootNavHost"
    ) {
        authGraph(navController = navHostController)
        surveyGraph(navController = navHostController)
    }
}
