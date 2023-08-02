package com.apsl.surveyapp.core.data.survey

import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.core.models.SurveyStatus
import com.apsl.surveyapp.core.realtime.models.SurveyRealtimeDbModel

fun Survey.toRealtimeDbModel(): SurveyRealtimeDbModel {
    return SurveyRealtimeDbModel(
        id = this.id,
        userId = this.userId,
        title = this.title,
        status = this.status.ordinal,
        imageUrl1 = this.imageUrl1,
        imageUrl2 = this.imageUrl2,
        votesCount1 = this.votesCount1,
        votesCount2 = this.votesCount2
    )
}

fun SurveyRealtimeDbModel.toDataModel(currentUserId: String? = null): Survey {
    val isVotedByCurrentUser = if (currentUserId != null) {
        currentUserId in this.votedUserIds1 || currentUserId in this.votedUserIds2
    } else {
        false
    }

    return Survey(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        status = requireNotNull(SurveyStatus.entries.getOrNull(this.status)),
        imageUrl1 = this.imageUrl1,
        imageUrl2 = this.imageUrl2,
        votesCount1 = this.votesCount1,
        votesCount2 = this.votesCount2,
        isVotedByCurrentUser = isVotedByCurrentUser,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
