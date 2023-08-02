package com.apsl.surveyapp.feature.survey.create

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apsl.surveyapp.core.data.survey.SurveyRepository
import com.apsl.surveyapp.core.data.user.UserRepository
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
class CreateSurveyViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSurveyUiState())
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<CreateSurveyAction>()
    val actions = _actions.asSharedFlow()

    init {
        getAllSurveyImages()
    }

    private fun getAllSurveyImages() {
        _uiState.update { it.copy(surveyImages = listOf(SurveyImage(0), SurveyImage(1))) }
    }

    fun createSurvey() {
        val userId = userRepository.currentUser?.id
        val titleTextFieldValue = uiState.value.titleTextFieldValue
        val descriptionTextFieldValue = uiState.value.descriptionTextFieldValue
        val imageUri1 = uiState.value.surveyImages.getOrNull(0)?.uri
        val imageUri2 = uiState.value.surveyImages.getOrNull(1)?.uri
        if (userId == null || titleTextFieldValue == null || imageUri1 == null || imageUri2 == null || descriptionTextFieldValue == null) {
            return
        }

        viewModelScope.launch {
            showLoading()
            runCatching {
                surveyRepository.createSurvey(
                    title = titleTextFieldValue.trim(),
                    description = descriptionTextFieldValue.trim(),
                    imageUri1 = imageUri1,
                    imageUri2 = imageUri2
                )
            }
                .onSuccess {
                    _actions.emit(CreateSurveyAction.NavigateAfterSuccess)
                    hideLoading()
                }
                .onFailure {
                    Timber.d(it)
                    _actions.emit(CreateSurveyAction.ShowError(""))
                    hideLoading()
                }
        }
    }

    fun updateTitleTextFieldValue(value: String?) {
        _uiState.update { it.copy(titleTextFieldValue = value) }
    }

    fun updateDescriptionTextFieldValueTextFieldValue(value: String?) {
        _uiState.update { it.copy(descriptionTextFieldValue = value) }
    }

    fun onImagePick(uri: String?, id: Int) {
        if (uri == null || !uiState.value.surveyImages.any { it.id == id }) {
            return
        }

        _uiState.update { state ->
            state.copy(
                surveyImages = state.surveyImages.map { image ->
                    if (image.id == id) image.copy(uri = uri) else image
                }
            )
        }
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun hideLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }
}

@Immutable
data class CreateSurveyUiState(
    val isLoading: Boolean = false,
    val surveyImages: List<SurveyImage> = emptyList(),
    val titleTextFieldValue: String? = null,
    val descriptionTextFieldValue: String? = null
) {
    val isCreateButtonEnabled: Boolean
        get() = !titleTextFieldValue.isNullOrBlank() && !surveyImages.any { it.uri == null }
}

data class SurveyImage(val id: Int, val uri: String? = null)

sealed interface CreateSurveyAction {
    data class ShowError(val message: String) : CreateSurveyAction
    data object NavigateAfterSuccess : CreateSurveyAction
}
