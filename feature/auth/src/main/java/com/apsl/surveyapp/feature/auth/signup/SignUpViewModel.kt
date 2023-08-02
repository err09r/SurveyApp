package com.apsl.surveyapp.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apsl.surveyapp.core.data.user.UserRepository
import com.apsl.surveyapp.core.models.Email
import com.apsl.surveyapp.core.models.Password
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<SignUpAction>()
    val actions = _actions.asSharedFlow()

    fun signUp() {
        val emailTextFieldValue = uiState.value.emailTextFieldValue
        val passwordTextFieldValue = uiState.value.passwordTextFieldValue
        if (emailTextFieldValue == null || passwordTextFieldValue == null) {
            return
        }

        viewModelScope.launch {
            showLoading()

            runCatching {
                userRepository.createUser(
                    email = Email(emailTextFieldValue),
                    password = Password(passwordTextFieldValue)
                )
            }
                .onSuccess { hideLoading() }
                .onFailure { throwable ->
                    Timber.d(throwable)
                    _actions.emit(SignUpAction.ShowError(""))
                    hideLoading()
                }
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun updateEmailTextFieldValue(value: String?) {
        _uiState.update { it.copy(emailTextFieldValue = value) }
    }

    fun updatePasswordTextFieldValue(value: String?) {
        _uiState.update { it.copy(passwordTextFieldValue = value) }
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun hideLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val emailTextFieldValue: String? = null,
    val passwordTextFieldValue: String? = null,
    val isPasswordVisible: Boolean = false
) {
    val isSignUpButtonEnabled: Boolean
        get() = !emailTextFieldValue.isNullOrBlank() && !passwordTextFieldValue.isNullOrBlank()
}

sealed interface SignUpAction {
    data class ShowError(val message: String) : SignUpAction
}
