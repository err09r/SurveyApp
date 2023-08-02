package com.apsl.surveyapp.feature.survey.mysurveys

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.EmptyScreen
import com.apsl.surveyapp.core.ui.FeatureScreen
import com.apsl.surveyapp.core.ui.components.ActionButton
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.feature.survey.common.SurveyListItem
import com.apsl.surveyapp.feature.survey.common.SurveyUiModel
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun MySurveysScreen(
    viewModel: MySurveysViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSurvey: (String) -> Unit,
    onNavigateToCreateSurvey: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MySurveysScreenContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onSurveyClick = onNavigateToSurvey,
        onCreateSurveyClick = onNavigateToCreateSurvey
    )
}

@Composable
fun MySurveysScreenContent(
    uiState: MySurveysUiState,
    onBackClick: () -> Unit,
    onSurveyClick: (String) -> Unit,
    onCreateSurveyClick: () -> Unit
) {
    FeatureScreen(topBarText = stringResource(CoreR.string.my_surveys), onBackClick = onBackClick) {
        if (uiState.surveys.isEmpty()) {
            EmptyScreen(text = stringResource(CoreR.string.no_surveys_empty_message)) {
                ActionButton(
                    onClick = onCreateSurveyClick,
                    text = stringResource(CoreR.string.create)
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = uiState.surveys, key = SurveyUiModel::id) { survey ->
                    SurveyListItem(survey = survey, onClick = { onSurveyClick(survey.id) })
                }
            }
        }
    }
}

@Preview
@Composable
fun MySurveysScreenPreview() {
    SurveyAppTheme {
        MySurveysScreenContent(
            uiState = MySurveysUiState(),
            onBackClick = {},
            onSurveyClick = {},
            onCreateSurveyClick = {}
        )
    }
}
