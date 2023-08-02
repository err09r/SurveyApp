package com.apsl.surveyapp.feature.survey.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.apsl.surveyapp.feature.survey.create.CreateSurveyScreen
import com.apsl.surveyapp.feature.survey.details.SurveyDetailsScreen
import com.apsl.surveyapp.feature.survey.allsurveys.AllSurveysScreen
import com.apsl.surveyapp.feature.survey.mysurveys.MySurveysScreen
import com.apsl.surveyapp.feature.survey.photo.SurveyImageScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.surveyGraph(navController: NavController) {
    navigation(startDestination = "allSurveys", route = "surveyGraph") {
        composable(route = "allSurveys") {
            AllSurveysScreen(
                onNavigateToCreateSurvey = { navController.navigate("createSurvey") },
                onNavigateToMySurveys = { navController.navigate("mySurveys") },
                onNavigateToSurvey = { id -> navController.navigate("survey/$id") }
            )
        }
        composable(route = "createSurvey") {
            CreateSurveyScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = "survey/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            SurveyDetailsScreen(
                id = requireNotNull(backStackEntry.arguments?.getString("id")),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSurveyImage = { uri ->
                    val encodedUri = URLEncoder.encode(uri, StandardCharsets.UTF_8.toString())
                    navController.navigate("surveyImage/$encodedUri")
                }
            )
        }
        composable(route = "mySurveys") {
            MySurveysScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSurvey = { id -> navController.navigate("survey/$id") },
                onNavigateToCreateSurvey = { navController.navigate("createSurvey") }
            )
        }
        composable(
            route = "surveyImage/{uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStackEntry ->
            SurveyImageScreen(
                uri = requireNotNull(backStackEntry.arguments?.getString("uri")),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
