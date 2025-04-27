package com.jakondev.a2048_game.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
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
