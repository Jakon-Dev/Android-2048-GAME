package com.jakondev.a2048_game.ui.theme.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.game2048.R


val Rowdies = FontFamily(
    Font(R.font.rowdies_light, FontWeight.Light),
    Font(R.font.rowdies_regular, FontWeight.Normal),
    Font(R.font.rowdies_bold, FontWeight.Bold)
)


val DarkColorPalette = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    secondary = SecondaryDark,
    onSecondary = Color.White,
    tertiary = TertiaryDark,
    onTertiary = Color.Black,
    background = PrimaryDark,
    onBackground = Color.White,
    surface = TileDark,
    onSurface = Color.White,
    surfaceVariant = SurfaceVariantDark,
    error = ErrorDark,
    onError = Color.Black
)

val LightColorPalette = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Color.Black,
    secondary = SecondaryLight,
    onSecondary = Color.Black,
    tertiary = TertiaryLight,
    onTertiary = Color.White,
    background = PrimaryLight,
    onBackground = Color.Black,
    surface = TileLight,
    onSurface = Color.Black,
    surfaceVariant = SurfaceVariantLight,
    error = ErrorLight,
    onError = Color.White
)

@Composable
fun getPalette(): ColorScheme {
    return if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
