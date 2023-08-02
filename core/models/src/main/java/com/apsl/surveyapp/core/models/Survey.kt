package com.apsl.surveyapp.core.models

data class Survey(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val status: SurveyStatus,
    val imageUrl1: String,
    val imageUrl2: String,
    val votesCount1: Int,
    val votesCount2: Int,
    val isVotedByCurrentUser: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
