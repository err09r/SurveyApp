package com.apsl.surveyapp.feature.survey.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme

@Composable
fun VotedImageWithBadge(
    value: String,
    url: String?,
    modifier: Modifier = Modifier,
    showBadge: Boolean = true
) {
    Box(modifier = modifier) {
        SurveyImage(url = url)
        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .background(
                        color = Color(red = 31, green = 30, blue = 32, alpha = 0xE6),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
fun VotedImageWithBadgePreview() {
    SurveyAppTheme {
        VotedImageWithBadge(value = "999+", url = null)
    }
}
