package com.jakondev.game2048.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakondev.game2048.util.getTileColor

@Composable
fun Tile(value: Int) {
    val scale by animateFloatAsState(
        targetValue = if (value != 0) 1f else 0f,
        label = "ScaleAnimation"
    )
    val backgroundColor by animateColorAsState(
        targetValue = getTileColor(value),
        label = "ColorAnimation"
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ValueTransition"
        ) { targetValue ->
            if (targetValue != 0) {
                Text(
                    text = targetValue.toString(),
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
        }
    }
}
