package com.apsl.surveyapp.feature.survey.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.feature.survey.common.SurveyImage

@Composable
fun VotableImage(selected: Boolean, url: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.let {
            if (selected) {
                it.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
            } else {
                it
            }
        }
    ) {
        SurveyImage(url = url)
        Box(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .padding(4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(red = 31, green = 30, blue = 32, alpha = 0xE6),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun VotableImageSelectedPreview() {
    SurveyAppTheme {
        VotableImage(selected = true, url = null)
    }
}

@Preview
@Composable
fun VotableImageUnselectedPreview() {
    SurveyAppTheme {
        VotableImage(selected = false, url = null)
    }
}
