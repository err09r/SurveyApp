package com.apsl.surveyapp.feature.survey.common

import com.apsl.surveyapp.core.models.Survey
import com.apsl.surveyapp.core.util.kotlin.DateFormats
import java.util.Date

data class SurveyUiModel(
    val id: String,
    val title: String,
    val imageUrl1: String,
    val imageUrl2: String,
    val date: String,
    val votesCount1: String,
    val votesCount2: String,
    val isBadgeVisible: Boolean
)

fun Survey.toSurveyUiModel(): SurveyUiModel {
    return SurveyUiModel(
        id = this.id,
        title = this.title,
        imageUrl1 = this.imageUrl1,
        imageUrl2 = this.imageUrl2,
        date = timestampToDateString(this.createdAt),
        votesCount1 = formatVotesCount(this.votesCount1),
        votesCount2 = formatVotesCount(this.votesCount2),
        isBadgeVisible = !this.isVotedByCurrentUser
    )
}

private fun timestampToDateString(timestamp: Long): String {
    return DateFormats.surveyDateFormat.format(Date(timestamp))
}

private fun formatVotesCount(votesCount: Int): String {
    if (votesCount > 999) {
        return "999+"
    }
    return votesCount.coerceAtLeast(0).toString()
}
