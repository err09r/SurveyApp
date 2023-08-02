package com.apsl.surveyapp.feature.survey.allsurveys

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apsl.surveyapp.core.data.auth.AuthRepository
import com.apsl.surveyapp.core.data.survey.SurveyRepository
import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.feature.survey.common.SurveyUiModel
import com.apsl.surveyapp.feature.survey.common.toSurveyUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AllSurveysViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllSurveysUiState())
    val uiState = _uiState.asStateFlow()

    fun getAllSurveys() {
        viewModelScope.launch {
            showLoading()
            surveyRepository.getAllSurveys().collect { surveys ->
                _uiState.update {
                    it.copy(isLoading = false, surveys = surveys.map(Survey::toSurveyUiModel))
                }
            }
        }
    }

    fun signOut() {
        runCatching { authRepository.signOut() }.onFailure(Timber::d)
    }

    fun updateSelectedDrawerItemIndex(index: Int) {
        _uiState.update { it.copy(selectedDrawerItemIndex = index) }
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }
}

@Immutable
data class AllSurveysUiState(
    val isLoading: Boolean = false,
    val surveys: List<SurveyUiModel> = emptyList(),
    val selectedDrawerItemIndex: Int = 0
)
