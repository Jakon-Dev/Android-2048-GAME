package com.jakondev.a2048_game.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.ui.theme.tiles.getTileColor
import com.jakondev.a2048_game.ui.theme.tiles.getTileTextColor

@Composable
fun GameBoard(
    board: Array<IntArray>,
    width: Dp,
    height: Dp
) {
    val rows = board.size
    val columns = board.firstOrNull()?.size ?: return

    // Board spacing and sizing
    val padding = width * 0.04f
    val spacing = width * 0.02f

    val cellWidth = (width - 2 * padding - spacing * (columns - 1)) / columns
    val cellHeight = (height - 2 * padding - spacing * (rows - 1)) / rows

    val cornerRadius = height * 0.025f

    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(
                color = getPalette().secondary,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        board.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { value ->
                    GameTile(
                        value = value,
                        width = cellWidth,
                        height = cellHeight
                    )
                }
            }
        }
    }
}


@Composable
fun GameTile(
    value: Int,
    width: Dp,
    height: Dp
) {
    val scale by animateFloatAsState(
        targetValue = if (value != 0) 1f else 0f,
        label = "TileScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = getTileColor(value),
        label = "TileColor"
    )

    BoxWithConstraints(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        val size = maxWidth
        val cornerRadius = size * 0.1f
        val fontSize = size.value * 0.35f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "TileValue"
            ) { targetValue ->
                if (targetValue != 0) {
                    Text(
                        text = targetValue.toString(),
                        fontFamily = Rowdies,
                        fontSize = fontSize.sp,
                        color = getTileTextColor(value)
                    )
                }
            }
        }
    }
}
