package com.apsl.surveyapp.feature.survey.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.FeatureScreen
import com.apsl.surveyapp.core.ui.LoadingScreen
import com.apsl.surveyapp.core.ui.components.ActionButton
import com.apsl.surveyapp.core.ui.components.GhostButton
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun SurveyDetailsScreen(
    id: String,
    viewModel: SurveyViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSurveyImage: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getSurveyDetails(id)
        viewModel.actions.collect { action ->
            when (action) {
                is SurveyDetailsAction.ShowError -> Unit
                is SurveyDetailsAction.NavigateAfterSuccess -> onNavigateBack()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SurveyDetailsScreenContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onDeleteClick = viewModel::deleteSurvey,
        onFinishClick = viewModel::closeSurvey,
        onVoteClick = viewModel::vote,
        onUpdateSelectedOption = viewModel::updateSelectedOption,
        onImageLongClick = onNavigateToSurveyImage
    )
}

@Composable
fun SurveyDetailsScreenContent(
    uiState: SurveyDetailsUiState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFinishClick: () -> Unit,
    onVoteClick: () -> Unit,
    onUpdateSelectedOption: (Int?) -> Unit,
    onImageLongClick: (String) -> Unit
) {
    FeatureScreen(
        topBarText = stringResource(CoreR.string.survey),
        onBackClick = onBackClick
    ) {
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.survey != null -> {
                AnimatedContent(targetState = uiState.uiMode, label = "") { uiMode ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    ) {
                        when (uiMode) {
                            is SurveyDetailsUiMode.Vote -> {
                                SurveyDetailsVotableContent(
                                    survey = uiState.survey,
                                    selectedOption = uiState.selectedOption,
                                    isVoteButtonEnabled = uiState.isVoteButtonEnabled,
                                    onUpdateSelectedOption = onUpdateSelectedOption,
                                    onVoteClick = onVoteClick,
                                    onImageLongClick = onImageLongClick
                                )
                            }

                            is SurveyDetailsUiMode.Edit -> {
                                SurveyDetailsEditableContent(
                                    survey = uiState.survey,
                                    onDeleteClick = onDeleteClick,
                                    onFinishClick = onFinishClick,
                                    onImageLongClick = onImageLongClick
                                )
                            }

                            is SurveyDetailsUiMode.View -> {
                                SurveyDetailsViewableContent(
                                    survey = uiState.survey,
                                    text = uiMode.message,
                                    onImageLongClick = onImageLongClick
                                )
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.SurveyDetailsVotableContent(
    survey: SurveyDetailsUiModel,
    isVoteButtonEnabled: Boolean,
    selectedOption: Int?,
    onUpdateSelectedOption: (Int?) -> Unit,
    onVoteClick: () -> Unit,
    onImageLongClick: (String) -> Unit
) {
    Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(16.dp))
    Text(
        text = survey.description,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(24.dp))
    Text(text = "Wybierz najliepszą opcję: ", style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(16.dp))
    VotableImage(
        selected = selectedOption == 0,
        url = survey.imageUrl1,
        modifier = Modifier.combinedClickable(
            onClick = { onUpdateSelectedOption(0) },
            onLongClick = { onImageLongClick(survey.imageUrl1) }
        )
    )
    Spacer(Modifier.padding(8.dp))
    VotableImage(
        selected = selectedOption == 1,
        url = survey.imageUrl2,
        modifier = Modifier.combinedClickable(
            onClick = { onUpdateSelectedOption(1) },
            onLongClick = { onImageLongClick(survey.imageUrl2) }
        )
    )
    Spacer(Modifier.weight(1f))
    Spacer(Modifier.height(32.dp))
    ActionButton(
        onClick = onVoteClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = isVoteButtonEnabled,
        text = stringResource(CoreR.string.vote)
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
fun ColumnScope.SurveyDetailsEditableContent(
    survey: SurveyDetailsUiModel,
    onDeleteClick: () -> Unit,
    onFinishClick: () -> Unit,
    onImageLongClick: (String) -> Unit
) {
    Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(16.dp))
    Text(
        text = survey.description,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(24.dp))
    VotedImage(
        value = survey.votesCount1,
        url = survey.imageUrl1,
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = { onImageLongClick(survey.imageUrl1) }
        )
    )
    Spacer(Modifier.padding(8.dp))
    VotedImage(
        value = survey.votesCount2,
        url = survey.imageUrl2,
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = { onImageLongClick(survey.imageUrl2) }
        )
    )
    Spacer(Modifier.weight(1f))
    Spacer(Modifier.height(32.dp))
    GhostButton(
        onClick = onDeleteClick,
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(CoreR.string.delete)
    )
    Spacer(Modifier.height(16.dp))
    ActionButton(
        onClick = onFinishClick,
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(CoreR.string.close)
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
fun ColumnScope.SurveyDetailsViewableContent(
    survey: SurveyDetailsUiModel,
    text: String,
    onImageLongClick: (String) -> Unit
) {
    Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(16.dp))
    Text(
        text = survey.description,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(32.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Rounded.WarningAmber, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(Modifier.height(24.dp))
    VotedImage(
        value = survey.votesCount1,
        url = survey.imageUrl1,
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = { onImageLongClick(survey.imageUrl1) }
        )
    )
    Spacer(Modifier.height(8.dp))
    VotedImage(
        value = survey.votesCount2,
        url = survey.imageUrl2,
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = { onImageLongClick(survey.imageUrl2) }
        )
    )
    Spacer(Modifier.height(16.dp))
}

@Preview
@Composable
fun SurveyDetailsScreenVotablePreview() {
    SurveyAppTheme {
        SurveyDetailsScreenContent(
            uiState = SurveyDetailsUiState(
                survey = SurveyDetailsUiModel(
                    id = "",
                    title = " What color is the sky?",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                    imageUrl1 = "",
                    imageUrl2 = "",
                    votesCount1 = "99",
                    votesCount2 = "2"
                ),
                uiMode = SurveyDetailsUiMode.Vote
            ),
            onBackClick = {},
            onDeleteClick = {},
            onFinishClick = {},
            onVoteClick = {},
            onUpdateSelectedOption = {},
            onImageLongClick = {}
        )
    }
}

@Preview
@Composable
fun SurveyDetailsScreenEditablePreview() {
    SurveyAppTheme {
        SurveyDetailsScreenContent(
            uiState = SurveyDetailsUiState(
                survey = SurveyDetailsUiModel(
                    id = "",
                    title = " What color is the sky?",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                    imageUrl1 = "",
                    imageUrl2 = "",
                    votesCount1 = "3",
                    votesCount2 = "441"
                ),
                uiMode = SurveyDetailsUiMode.Edit
            ),
            onBackClick = {},
            onDeleteClick = {},
            onFinishClick = {},
            onVoteClick = {},
            onUpdateSelectedOption = {},
            onImageLongClick = {}
        )
    }
}

@Preview
@Composable
fun SurveyDetailsScreenViewablePreview() {
    SurveyAppTheme {
        SurveyDetailsScreenContent(
            uiState = SurveyDetailsUiState(
                survey = SurveyDetailsUiModel(
                    id = "",
                    title = " What color is the sky?",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                    imageUrl1 = "",
                    imageUrl2 = "",
                    votesCount1 = "99",
                    votesCount2 = "2"
                ),
                uiMode = SurveyDetailsUiMode.View("Już głosowałeś.")
            ),
            onBackClick = {},
            onDeleteClick = {},
            onFinishClick = {},
            onVoteClick = {},
            onUpdateSelectedOption = {},
            onImageLongClick = {}
        )
    }
}
