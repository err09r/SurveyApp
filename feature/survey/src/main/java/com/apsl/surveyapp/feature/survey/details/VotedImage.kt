package com.apsl.surveyapp.feature.survey.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.apsl.surveyapp.feature.survey.common.SurveyImage

@Composable
fun VotedImage(value: String, url: String?, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        SurveyImage(url = url)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(56.dp)
                .background(
                    color = Color(red = 31, green = 30, blue = 32, alpha = 0xE6),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun VotedImagePreview() {
    SurveyAppTheme {
        VotedImage(value = "999+", url = null)
    }
}
