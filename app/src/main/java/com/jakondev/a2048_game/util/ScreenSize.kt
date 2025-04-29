package com.jakondev.a2048_game.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenSize(
    val width: Dp,
    val height: Dp,
    val isLandscape: Boolean
)

@Composable
fun rememberScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current
    return ScreenSize(
        width = configuration.screenWidthDp.dp,
        height = configuration.screenHeightDp.dp,
        isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    )
}
