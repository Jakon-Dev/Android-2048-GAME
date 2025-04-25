package com.jakondev.a2048_game.ui.game

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.jakondev.game2048.ui.Tile

@Composable
fun GameBoard(board: Array<IntArray>, width: Dp, height: Dp) {
    val rows = board.size
    val columns = board.firstOrNull()?.size ?: 0

    Log.d("GameBoard", "Rows: $rows, Columns: $columns")

    val boardPadding = width * 0.02f // podrías usar width o height aquí, depende
    val cellSpacing = width * 0.02f // igual aquí, elegimos width como referencia

    Log.d("GameBoard", "Board Padding: $boardPadding, Cell Spacing: $cellSpacing")

    val availableWidth = width - (boardPadding * 2) - (cellSpacing * (columns + 3))
    val availableHeight = height - (boardPadding * 2) - (cellSpacing * (rows + 3))

    Log.d("GameBoard", "Available Width: $availableWidth, Available Height: $availableHeight")

    val cellWidth = availableWidth / columns
    val cellHeight = availableHeight / rows

    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFCDC1B4))
            .padding(boardPadding),
        verticalArrangement = Arrangement.spacedBy(cellSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        board.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(cellSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { value ->
                    Tile(
                        value,
                        Modifier
                            .width(cellWidth)
                            .height(cellHeight)
                    )
                }
            }
        }
    }
}


