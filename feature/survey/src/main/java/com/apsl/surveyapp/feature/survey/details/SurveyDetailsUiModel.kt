package com.apsl.surveyapp.feature.survey.details

import com.apsl.surveyapp.core.models.Survey

data class SurveyDetailsUiModel(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl1: String,
    val imageUrl2: String,
    val votesCount1: String,
    val votesCount2: String,
    val isVotedByCurrentUser: Boolean = false
)

fun Survey.toSurveyDetailsUiModel(): SurveyDetailsUiModel {
    return SurveyDetailsUiModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl1 = this.imageUrl1,
        imageUrl2 = this.imageUrl2,
        votesCount1 = formatVotesCount(this.votesCount1),
        votesCount2 = formatVotesCount(this.votesCount2),
        isVotedByCurrentUser = this.isVotedByCurrentUser
    )
}

private fun formatVotesCount(votesCount: Int): String {
    if (votesCount > 999) {
        return "999+"
    }
    return votesCount.coerceAtLeast(0).toString()
}
