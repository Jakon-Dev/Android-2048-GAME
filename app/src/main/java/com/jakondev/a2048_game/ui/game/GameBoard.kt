package com.jakondev.a2048_game.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jakondev.game2048.ui.Tile

@Composable
fun GameBoard(board: Array<IntArray>, size: Dp) {
    val cellSize = size / board.size

    Column(
        modifier = Modifier
            .size(size)
            .background(Color(0xFFCDC1B4))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        board.forEach { row ->
            Row {
                row.forEach { value ->
                    Tile(value, Modifier.size(cellSize))
                }
            }
        }
    }
}