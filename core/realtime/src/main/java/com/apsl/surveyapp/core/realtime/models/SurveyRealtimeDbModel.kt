package com.apsl.surveyapp.core.realtime.models

data class SurveyRealtimeDbModel(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val status: Int = 0,
    val imageUrl1: String = "",
    val imageUrl2: String = "",
    val votesCount1: Int = 0,
    val votesCount2: Int = 0,
    val votedUserIds1: List<String> = emptyList(),
    val votedUserIds2: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
