package com.apsl.surveyapp.core.realtime

import com.apsl.surveyapp.core.realtime.models.SurveyRealtimeDbModel
import kotlinx.coroutines.flow.Flow

interface FirebaseRealtimeDatabaseApi {
    fun getAllSurveys(): Flow<Result<List<SurveyRealtimeDbModel>>>
    fun getAllSurveysByUserId(id: String): Flow<Result<List<SurveyRealtimeDbModel>>>
    fun getSurveyById(id: String): Flow<Result<SurveyRealtimeDbModel>>
    suspend fun createSurvey(survey: SurveyRealtimeDbModel)
    suspend fun updateSurvey(id: String, status: Int)
    suspend fun updateSurvey(id: String, votesCount1: Int, votesCount2: Int, votedUserId: String)
    suspend fun deleteSurvey(id: String)
}
