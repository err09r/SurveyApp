package com.apsl.surveyapp.core.realtime

import com.apsl.surveyapp.core.realtime.models.SurveyRealtimeDbModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseRealtimeDatabaseApiImpl @Inject constructor() : FirebaseRealtimeDatabaseApi {

    private val firebaseDatabase by lazy { Firebase.database(BuildConfig.REALTIME_DB_URL) }

    private val surveysReference by lazy { firebaseDatabase.getReference("surveys") }

    override fun getAllSurveys() = callbackFlow<Result<List<SurveyRealtimeDbModel>>> {
        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val surveys = snapshot.children
                    .mapNotNull {
                        it.getValue(SurveyRealtimeDbModel::class.java)
                    }
                    .sortedByDescending(SurveyRealtimeDbModel::createdAt)

                launch {
                    send(Result.success(surveys))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                launch {
                    send(Result.failure(Exception(error.message)))
                }
            }
        }

        surveysReference.addValueEventListener(listener)

        awaitClose { surveysReference.removeEventListener(listener) }

    }.flowOn(Dispatchers.IO)

    override fun getAllSurveysByUserId(id: String): Flow<Result<List<SurveyRealtimeDbModel>>> {
        return callbackFlow<Result<List<SurveyRealtimeDbModel>>> {
            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val surveys = snapshot.children
                        .mapNotNull {
                            it.getValue(SurveyRealtimeDbModel::class.java)
                        }
                        .sortedByDescending(SurveyRealtimeDbModel::createdAt)

                    launch {
                        send(Result.success(surveys))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    launch {
                        send(Result.failure(Exception(error.message)))
                    }
                }
            }

            val reference = surveysReference.orderByChild("userId").equalTo(id)

            reference.addValueEventListener(listener)

            awaitClose { reference.removeEventListener(listener) }

        }.flowOn(Dispatchers.IO)
    }

    override fun getSurveyById(id: String) = callbackFlow<Result<SurveyRealtimeDbModel>> {
        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(SurveyRealtimeDbModel::class.java)

                launch {
                    val elementToSend = if (survey != null) {
                        Result.success(survey)
                    } else {
                        Result.failure(Exception("Entry does not exist"))
                    }
                    send(elementToSend)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                launch {
                    send(Result.failure(Exception(error.message)))
                }
            }
        }

        val reference = surveysReference.child(id)

        reference.addValueEventListener(listener)

        awaitClose { reference.removeEventListener(listener) }

    }.flowOn(Dispatchers.IO)

    override suspend fun createSurvey(survey: SurveyRealtimeDbModel) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            surveysReference
                .child(survey.id)
                .setValue(survey)
                .addOnCompleteListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun updateSurvey(id: String, status: Int) {
        updateSurvey(
            id = id,
            updatedChildren = mapOf("status" to status, "updatedAt" to System.currentTimeMillis())
        )
    }

    override suspend fun updateSurvey(
        id: String,
        votesCount1: Int,
        votesCount2: Int,
        votedUserId: String
    ) = withContext(Dispatchers.IO) {
        val updatedChildren = mutableMapOf<String, Any>(
            "votesCount1" to votesCount1,
            "votesCount2" to votesCount2,
            "updatedAt" to System.currentTimeMillis()
        )

        val surveyBeforeUpdate = getSurveyById(id).firstOrNull()?.getOrNull()
        if (surveyBeforeUpdate != null) {
            when {
                surveyBeforeUpdate.votesCount1 < votesCount1 -> {
                    updatedChildren["votedUserIds1"] =
                        (surveyBeforeUpdate.votedUserIds1 + votedUserId).distinct()
                }

                surveyBeforeUpdate.votesCount2 < votesCount2 -> {
                    updatedChildren["votedUserIds2"] =
                        (surveyBeforeUpdate.votedUserIds2 + votedUserId).distinct()
                }
            }

            updateSurvey(id = id, updatedChildren = updatedChildren)
        }
    }

    private suspend fun updateSurvey(
        id: String,
        updatedChildren: Map<String, Any>
    ) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            surveysReference
                .child(id)
                .updateChildren(updatedChildren)
                .addOnCompleteListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun deleteSurvey(id: String) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            surveysReference
                .child(id)
                .setValue(null)
                .addOnCompleteListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}
