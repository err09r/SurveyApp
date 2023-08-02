package com.apsl.surveyapp.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun Color.Companion.random(): Color {
    return Color(
        red = Random.nextInt(0, 256),
        green = Random.nextInt(0, 256),
        blue = Random.nextInt(0, 256)
    )
}

fun Modifier.rippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = if (isSystemInDarkTheme()) Color.White else Color.Black),
        enabled = enabled,
        onClick = onClick
    )
}

fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick
    )
}
