package com.apsl.surveyapp.feature.auth.signin

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.LoadingScreen
import com.apsl.surveyapp.core.ui.components.ActionButton
import com.apsl.surveyapp.core.ui.components.BaseTextField
import com.apsl.surveyapp.core.ui.components.PasswordTextField
import com.apsl.surveyapp.core.ui.rippleClickable
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.feature.auth.common.AuthScreen
import kotlinx.coroutines.launch
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is SignInAction.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Niepoprawny email lub hasło")
                    }
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignInScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEmailTextFieldValueChange = viewModel::updateEmailTextFieldValue,
        onPasswordTextFieldValueChange = viewModel::updatePasswordTextFieldValue,
        onTogglePasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onSignInClick = viewModel::signIn,
        onDontHaveAccountClick = onNavigateToSignUp
    )
}

@Composable
fun SignInScreenContent(
    uiState: SignInUiState,
    snackbarHostState: SnackbarHostState,
    onEmailTextFieldValueChange: (String?) -> Unit,
    onPasswordTextFieldValueChange: (String?) -> Unit,
    onTogglePasswordVisibilityClick: () -> Unit,
    onSignInClick: () -> Unit,
    onDontHaveAccountClick: () -> Unit
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
                        text = stringResource(CoreR.string.sign_in),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(32.dp))
                    BaseTextField(
                        value = uiState.emailTextFieldValue ?: "",
                        onValueChange = onEmailTextFieldValueChange,
                        modifier = Modifier.fillMaxWidth(),
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
                        onClick = onSignInClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isSignInButtonEnabled,
                        text = stringResource(CoreR.string.sign_in)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(CoreR.string.dont_have_account),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .rippleClickable(onClick = onDontHaveAccountClick),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SurveyAppTheme {
        SignInScreenContent(
            uiState = SignInUiState(
                emailTextFieldValue = "test@mail.com",
                passwordTextFieldValue = "12345"
            ),
            snackbarHostState = SnackbarHostState(),
            onEmailTextFieldValueChange = {},
            onPasswordTextFieldValueChange = {},
            onTogglePasswordVisibilityClick = {},
            onSignInClick = {},
            onDontHaveAccountClick = {}
        )
    }
}
