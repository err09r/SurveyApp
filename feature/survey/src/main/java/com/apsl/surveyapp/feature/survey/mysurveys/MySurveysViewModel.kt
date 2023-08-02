package com.apsl.surveyapp.feature.survey.mysurveys

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apsl.surveyapp.core.data.survey.SurveyRepository
import com.apsl.surveyapp.core.data.user.UserRepository
import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.feature.survey.common.SurveyUiModel
import com.apsl.surveyapp.feature.survey.common.toSurveyUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MySurveysViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MySurveysUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSurveysByUser()
    }

    private fun getSurveysByUser() {
        val userId = userRepository.currentUser?.id ?: return
        viewModelScope.launch {
            showLoading()
            surveyRepository.getAllSurveysByUserId(userId).collect { surveys ->
                _uiState.update {
                    it.copy(isLoading = false, surveys = surveys.map(Survey::toSurveyUiModel))
                }
            }
        }
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }
}

@Immutable
data class MySurveysUiState(
    val isLoading: Boolean = false,
    val surveys: List<SurveyUiModel> = emptyList()
)
