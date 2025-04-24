package com.jakondev.game2048.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription

@Composable
fun DirectionControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
        ) {
            StylizedButton("↑", onUp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
        ) {
            StylizedButton("←", onLeft)
            Spacer(modifier = Modifier.width(16.dp))
            StylizedButton("↓", onDown)
            Spacer(modifier = Modifier.width(16.dp))
            StylizedButton("→", onRight)
        }
    }
}

@Composable
fun StylizedButton(
    text: String,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val buttonColor = Color(0xFFD3D3D3) // Light grey
    val outlineColor = Color.Black
    val cornerRadius = 12.dp
    val buttonSize = 85.dp
    val outlineSize = 90.dp

    val offsetY by animateFloatAsState(
        targetValue = if (pressed) 0f else -7f,
        animationSpec = tween(durationMillis = 90),
        label = "buttonOffsetY"
    )
    val outlineHeight by animateFloatAsState(
        targetValue = if (pressed) outlineSize.value - 16 else outlineSize.value - 5,
        animationSpec = tween(durationMillis = 90),
        label = "outlineHeight"
    )

    Box(
        modifier = Modifier
            .offset(y = 3.dp)
            .background(outlineColor, RoundedCornerShape(cornerRadius))
            .width(outlineSize) // Mantiene el mismo ancho
            .height(outlineHeight.dp) // Cambia la altura animadamente
            .clip(RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(y = offsetY.dp)
                .width(buttonSize)
                .height(buttonSize - 10.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(buttonColor)
                .border(2.dp, outlineColor, RoundedCornerShape(cornerRadius))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            try {
                                awaitRelease()
                                onClick()
                            } finally {
                                pressed = false
                            }
                        }
                    )
                }
                .semantics { contentDescription = "Direction $text" }
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = outlineColor
            )
        }
    }
}
