package com.jakondev.game2048.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getTileColor(value: Int): Color {
    val isDark = isSystemInDarkTheme()

    return when (value) {
        0 -> if (isDark) EmptyTileDark else EmptyTileLight
        2 -> if (isDark) Tile2Dark else Tile2Light
        4 -> if (isDark) Tile4Dark else Tile4Light
        8 -> if (isDark) Tile8Dark else Tile8Light
        16 -> if (isDark) Tile16Dark else Tile16Light
        32 -> if (isDark) Tile32Dark else Tile32Light
        64 -> if (isDark) Tile64Dark else Tile64Light
        128 -> if (isDark) Tile128Dark else Tile128Light
        256 -> if (isDark) Tile256Dark else Tile256Light
        512 -> if (isDark) Tile512Dark else Tile512Light
        1024 -> if (isDark) Tile1024Dark else Tile1024Light
        2048 -> if (isDark) Tile2048Dark else Tile2048Light
        else -> if (isDark) SuperTileDark else SuperTileLight // para tiles mayores
    }
}
