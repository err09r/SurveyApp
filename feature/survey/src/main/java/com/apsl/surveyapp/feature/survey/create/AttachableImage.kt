package com.apsl.surveyapp.feature.survey.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun AttachableImage(modifier: Modifier = Modifier, uri: String?, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.small
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(CoreR.drawable.img_placeholder),
                error = painterResource(CoreR.drawable.img_placeholder),
                fallback = painterResource(CoreR.drawable.img_placeholder),
                contentScale = ContentScale.Crop
            )
            if (uri == null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SurveyAttachedImagePreview() {
    SurveyAppTheme {
        AttachableImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            uri = null,
            onClick = {}
        )
    }
}
