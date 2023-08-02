package com.apsl.surveyapp.core.data.survey

import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.core.models.SurveyStatus
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun getAllSurveys(): Flow<List<Survey>>
    fun getAllSurveysByUserId(id: String): Flow<List<Survey>>
    fun getSurveyById(id: String): Flow<Survey>
    suspend fun createSurvey(title: String, description: String, imageUri1: String, imageUri2: String)
    suspend fun updateSurvey(id: String, status: SurveyStatus)
    suspend fun updateSurvey(id: String, votesCount1: Int, votesCount2: Int)
    suspend fun deleteSurvey(id: String)
}
