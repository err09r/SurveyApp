package com.apsl.surveyapp.core.data.survey

import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.core.models.SurveyStatus
import com.apsl.surveyapp.core.realtime.FirebaseRealtimeDatabaseApi
import com.apsl.surveyapp.core.realtime.models.SurveyRealtimeDbModel
import com.apsl.surveyapp.core.storage.FirebaseStorageApi
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class SurveyRepositoryImpl @Inject constructor(
    private val firebaseRealtimeDatabase: FirebaseRealtimeDatabaseApi,
    private val firebaseAuthApi: FirebaseAuthApi,
    private val firebaseStorageApi: FirebaseStorageApi
) : SurveyRepository {

    override fun getAllSurveys(): Flow<List<Survey>> {
        return firebaseRealtimeDatabase.getAllSurveys().mapNotNull { result ->
            val surveyRealtimeDbModels = result.getOrNull()
            surveyRealtimeDbModels?.map {
                it.toDataModel(currentUserId = firebaseAuthApi.currentUser?.uid)
            }
        }
    }

    override fun getAllSurveysByUserId(id: String): Flow<List<Survey>> {
        return firebaseRealtimeDatabase.getAllSurveysByUserId(id).mapNotNull { result ->
            val surveyRealtimeDbModels = result.getOrNull()
            surveyRealtimeDbModels?.map {
                it.toDataModel(currentUserId = firebaseAuthApi.currentUser?.uid)
            }
        }
    }

    override fun getSurveyById(id: String): Flow<Survey> {
        return firebaseRealtimeDatabase.getSurveyById(id).mapNotNull { result ->
            val surveyRealtimeDbModel = result.getOrNull()
            surveyRealtimeDbModel?.toDataModel(currentUserId = firebaseAuthApi.currentUser?.uid)
        }
    }

    override suspend fun createSurvey(
        title: String,
        description: String,
        imageUri1: String,
        imageUri2: String
    ) = coroutineScope {
        val userId = firebaseAuthApi.currentUser?.uid ?: return@coroutineScope

        val imageUrl1 = async {
            firebaseStorageApi.uploadFile(uri = imageUri1, folderName = userId)
        }
        val imageUrl2 = async {
            firebaseStorageApi.uploadFile(uri = imageUri2, folderName = userId)
        }

        val survey = SurveyRealtimeDbModel(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            description = description,
            status = SurveyStatus.Active.ordinal,
            imageUrl1 = imageUrl1.await(),
            imageUrl2 = imageUrl2.await(),
            votesCount1 = INITIAL_VOTES_COUNT,
            votesCount2 = INITIAL_VOTES_COUNT,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        firebaseRealtimeDatabase.createSurvey(survey)
    }

    override suspend fun updateSurvey(id: String, status: SurveyStatus) {
        firebaseRealtimeDatabase.updateSurvey(id = id, status = status.ordinal)
    }

    override suspend fun updateSurvey(id: String, votesCount1: Int, votesCount2: Int) {
        val userId = firebaseAuthApi.currentUser?.uid ?: return

        firebaseRealtimeDatabase.updateSurvey(
            id = id,
            votesCount1 = votesCount1,
            votesCount2 = votesCount2,
            votedUserId = userId
        )
    }

    override suspend fun deleteSurvey(id: String) {
        firebaseRealtimeDatabase.deleteSurvey(id)
    }

    private companion object {
        private const val INITIAL_VOTES_COUNT = 0
    }
}
