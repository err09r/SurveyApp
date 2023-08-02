package com.apsl.surveyapp.feature.survey.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apsl.surveyapp.core.data.survey.SurveyRepository
import com.apsl.surveyapp.core.data.user.UserRepository
import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.core.models.SurveyStatus
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
class SurveyViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurveyDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<SurveyDetailsAction>()
    val actions = _actions.asSharedFlow()

    private var surveyDataModel: Survey? = null

    fun getSurveyDetails(id: String) {
        viewModelScope.launch {
            showLoading()
            surveyRepository.getSurveyById(id).collect { survey ->
                Timber.d("Survey: $survey")
                _uiState.update {
                    surveyDataModel = survey
                    val surveyDetailsUiModel = survey.toSurveyDetailsUiModel()
                    it.copy(
                        isLoading = false,
                        survey = surveyDetailsUiModel,
                        areOptionsButtonsEnabled = !surveyDetailsUiModel.isVotedByCurrentUser,
                        uiMode = when {
                            survey.status == SurveyStatus.Active && survey.userId == userRepository.currentUser?.id -> SurveyDetailsUiMode.Edit
                            survey.status == SurveyStatus.Active && !survey.isVotedByCurrentUser -> {
                                SurveyDetailsUiMode.Vote
                            }

                            else -> {
                                SurveyDetailsUiMode.View(
                                    message = when {
                                        survey.status == SurveyStatus.Closed -> "Głosowanie jest zamknięte."
                                        else -> "Już głosowałeś."
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun closeSurvey() {
        val survey = uiState.value.survey ?: return
        viewModelScope.launch {
            surveyRepository.updateSurvey(id = survey.id, status = SurveyStatus.Closed)
        }
    }

    fun deleteSurvey() {
        val id = uiState.value.survey?.id ?: return
        viewModelScope.launch {
            runCatching { surveyRepository.deleteSurvey(id) }
                .onSuccess { _actions.emit(SurveyDetailsAction.NavigateAfterSuccess) }
                .onFailure {
                    Timber.d(it)
                    _actions.emit(SurveyDetailsAction.ShowError(it.message))
                }
        }
    }

    fun vote() {
        if (uiState.value.selectedOption == 0) {
            voteForOption1()
        } else {
            voteForOption2()
        }
    }

    fun updateSelectedOption(value: Int?) {
        _uiState.update {
            it.copy(selectedOption = if (value != null && value == uiState.value.selectedOption) null else value)
        }
    }

    private fun voteForOption1() {
        val survey = surveyDataModel ?: return
        viewModelScope.launch {
            surveyRepository.updateSurvey(
                id = survey.id,
                votesCount1 = survey.votesCount1 + 1,
                votesCount2 = survey.votesCount2
            )
        }
    }

    private fun voteForOption2() {
        val survey = surveyDataModel ?: return
        viewModelScope.launch {
            surveyRepository.updateSurvey(
                id = survey.id,
                votesCount1 = survey.votesCount1,
                votesCount2 = survey.votesCount2 + 1
            )
        }
    }

    private fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }
}

data class SurveyDetailsUiState(
    val isLoading: Boolean = false,
    val survey: SurveyDetailsUiModel? = null,
    val selectedOption: Int? = null,
    val areOptionsButtonsEnabled: Boolean = true,
    val uiMode: SurveyDetailsUiMode = SurveyDetailsUiMode.Initial
) {
    val isVoteButtonEnabled = areOptionsButtonsEnabled && selectedOption != null
}

sealed interface SurveyDetailsUiMode {
    data object Initial : SurveyDetailsUiMode
    data object Vote : SurveyDetailsUiMode
    data object Edit : SurveyDetailsUiMode
    data class View(val message: String) : SurveyDetailsUiMode
}

sealed interface SurveyDetailsAction {
    data class ShowError(val message: String?) : SurveyDetailsAction
    data object NavigateAfterSuccess : SurveyDetailsAction
}
