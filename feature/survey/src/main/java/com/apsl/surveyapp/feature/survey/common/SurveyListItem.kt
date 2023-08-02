package com.apsl.surveyapp.feature.survey.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme

@Composable
fun SurveyListItem(
    survey: SurveyUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = survey.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 19.sp)
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Event,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = survey.date,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VotedImageWithBadge(
                    value = survey.votesCount1,
                    url = survey.imageUrl1,
                    showBadge = survey.isBadgeVisible,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                )
                VotedImageWithBadge(
                    value = survey.votesCount2,
                    url = survey.imageUrl2,
                    showBadge = survey.isBadgeVisible,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SurveyItemPreview() {
    SurveyAppTheme {
        val survey = SurveyUiModel(
            id = "2",
            title = "What color is the sky?",
            imageUrl1 = "",
            imageUrl2 = "",
            date = "10-01-2023, 18:33",
            votesCount1 = "1",
            votesCount2 = "32",
            isBadgeVisible = false
        )
        SurveyListItem(
            survey = survey,
            onClick = {}
        )
    }
}
