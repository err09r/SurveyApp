package com.apsl.surveyapp.feature.auth.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.LoadingScreen
import com.apsl.surveyapp.core.ui.components.ActionButton
import com.apsl.surveyapp.core.ui.components.BaseTextField
import com.apsl.surveyapp.core.ui.components.PasswordTextField
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.feature.auth.common.AuthScreen
import kotlinx.coroutines.launch
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is SignUpAction.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Email lub hasło nie spełniają wymagań")
                    }
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEmailTextFieldValueChange = viewModel::updateEmailTextFieldValue,
        onPasswordTextFieldValueChange = viewModel::updatePasswordTextFieldValue,
        onTogglePasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onSignUpClick = viewModel::signUp
    )
}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    snackbarHostState: SnackbarHostState,
    onEmailTextFieldValueChange: (String?) -> Unit,
    onPasswordTextFieldValueChange: (String?) -> Unit,
    onTogglePasswordVisibilityClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(snackbarData = data, contentColor = MaterialTheme.colorScheme.onPrimary)
        }
    }) { paddingValues ->
        when {
            uiState.isLoading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            else -> {
                AuthScreen(modifier = Modifier.padding(paddingValues)) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(CoreR.string.sign_up),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(32.dp))
                    BaseTextField(
                        value = uiState.emailTextFieldValue ?: "",
                        onValueChange = onEmailTextFieldValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        label = stringResource(CoreR.string.email)
                    )
                    Spacer(Modifier.height(16.dp))
                    PasswordTextField(
                        value = uiState.passwordTextFieldValue ?: "",
                        onValueChange = onPasswordTextFieldValueChange,
                        passwordVisible = uiState.isPasswordVisible,
                        onTogglePasswordVisibilityClick = onTogglePasswordVisibilityClick,
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(CoreR.string.password),
                    )
                    Spacer(Modifier.height(32.dp))
                    ActionButton(
                        onClick = onSignUpClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isSignUpButtonEnabled,
                        text = stringResource(CoreR.string.sign_up)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SurveyAppTheme {
        SignUpScreenContent(
            uiState = SignUpUiState(),
            snackbarHostState = SnackbarHostState(),
            onEmailTextFieldValueChange = {},
            onPasswordTextFieldValueChange = {},
            onTogglePasswordVisibilityClick = {},
            onSignUpClick = {}
        )
    }
}
