package com.apsl.surveyapp.feature.survey.create

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.FeatureScreen
import com.apsl.surveyapp.core.ui.components.ActionButton
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun CreateSurveyScreen(
    viewModel: CreateSurveyViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is CreateSurveyAction.NavigateAfterSuccess -> onNavigateBack()
                else -> Unit
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CreateSurveyScreenContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onTitleTextFieldValueChange = viewModel::updateTitleTextFieldValue,
        onDescriptionTextFieldValueChange = viewModel::updateDescriptionTextFieldValueTextFieldValue,
        onCreateClick = viewModel::createSurvey,
        onImagePick = viewModel::onImagePick
    )
}

@Composable
fun CreateSurveyScreenContent(
    uiState: CreateSurveyUiState,
    onBackClick: () -> Unit,
    onTitleTextFieldValueChange: (String) -> Unit,
    onDescriptionTextFieldValueChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onImagePick: (String?, Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    FeatureScreen(
        topBarText = stringResource(CoreR.string.new_survey),
        modifier = Modifier.pointerInput(focusManager) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        onBackClick = onBackClick
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.titleTextFieldValue ?: "",
                    onValueChange = onTitleTextFieldValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    label = {
                        Text(
                            text = stringResource(CoreR.string.title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.descriptionTextFieldValue ?: "",
                    onValueChange = onDescriptionTextFieldValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    label = {
                        Text(
                            text = stringResource(CoreR.string.description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.surveyImages.forEach { image ->
                        val launcher = image.getLauncherForActivityResult(onResult = onImagePick)
                        AttachableImage(
                            modifier = Modifier.height(160.dp),
                            uri = image.uri,
                            onClick = {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(32.dp))
                ActionButton(
                    onClick = onCreateClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.isCreateButtonEnabled,
                    text = stringResource(CoreR.string.create)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SurveyImage.getLauncherForActivityResult(
    onResult: (String?, Int) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onResult(uri?.toString(), id) }
    )
}

@Preview
@Composable
fun CreateSurveyScreenPreview() {
    SurveyAppTheme {
        CreateSurveyScreenContent(
            uiState = CreateSurveyUiState(
                isLoading = true,
                surveyImages = listOf(SurveyImage(id = 0), SurveyImage(id = 1)),
                titleTextFieldValue = "Title",
                descriptionTextFieldValue = "Description"
            ),
            onBackClick = {},
            onTitleTextFieldValueChange = {},
            onDescriptionTextFieldValueChange = {},
            onCreateClick = {},
            onImagePick = { _, _ -> }
        )
    }
}
