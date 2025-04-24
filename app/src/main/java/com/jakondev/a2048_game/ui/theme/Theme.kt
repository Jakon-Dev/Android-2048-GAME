package com.jakondev.a2048_game.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorPalette = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    secondary = SecondaryDark,
    background = PrimaryDark,
    surface = TileDark,
    onSurface = Color.White
)

val LightColorPalette = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Color.Black,
    secondary = SecondaryLight,
    background = PrimaryLight,
    surface = TileLight,
    onSurface = Color.Black
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // si tienes uno personalizado
        content = content
    )
}
