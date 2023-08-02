package com.apsl.surveyapp.feature.survey.common

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apsl.surveyapp.core.ui.R
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme

@Composable
fun SurveyImage(url: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
            .height(160.dp)
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.img_placeholder),
        error = painterResource(R.drawable.img_placeholder),
        fallback = painterResource(R.drawable.img_placeholder)
    )
}

@Preview
@Composable
fun SurveyImagePreview() {
    SurveyAppTheme {
        SurveyImage(url = null)
    }
}
