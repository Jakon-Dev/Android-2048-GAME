package com.example.a2048_game.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// COLORES PRIMARIOS Y SECUNDARIOS
val DarkColorPalette = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark
)

val LightColorPalette = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
