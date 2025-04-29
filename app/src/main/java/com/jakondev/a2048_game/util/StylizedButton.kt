package com.jakondev.a2048_game.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakondev.a2048_game.ui.theme.Rowdies
import com.jakondev.a2048_game.ui.theme.getPalette

@Composable
fun StylizedButton(
    text: String,
    onClick: () -> Unit,
    size: Dp = 100.dp,
    outlineSize: Dp? = null,
    buttonWidth: Dp? = null,
    buttonHeight: Dp? = null,
    outlineWidth: Dp? = null,
    outlineHeight: Dp? = null,
    textSize: TextUnit = 50.sp,
    buttonColor: androidx.compose.ui.graphics.Color = getPalette().surface,
    outlineColor: androidx.compose.ui.graphics.Color = getPalette().tertiary,
) {
    var pressed by remember { mutableStateOf(false) }

    val cornerRadius = 12.dp

    val computedButtonWidth = buttonWidth ?: size
    val computedButtonHeight = buttonHeight ?: size

    val computedOutlineWidth = outlineWidth
        ?: (outlineSize?.times(0.95f) ?: computedButtonWidth.times(0.95f))
    val computedOutlineHeight = outlineHeight
        ?: (outlineSize ?: computedButtonHeight)

    val offsetY by animateFloatAsState(
        targetValue = if (pressed) 0f else -3f,
        animationSpec = tween(durationMillis = 90),
        label = "buttonOffsetY"
    )
    val animatedOutlineHeight by animateFloatAsState(
        targetValue = if (pressed) computedOutlineHeight.value - 8 else computedOutlineHeight.value - 5,
        animationSpec = tween(durationMillis = 90),
        label = "outlineHeight"
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(computedButtonWidth)
            .height(computedButtonHeight)
    ) {
        Box(
            modifier = Modifier
                .offset(y = 3.dp)
                .background(outlineColor, RoundedCornerShape(cornerRadius))
                .width(computedOutlineWidth)
                .height(animatedOutlineHeight.dp)
                .clip(RoundedCornerShape(cornerRadius)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .width(computedButtonWidth)
                    .height(computedButtonHeight)
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
                    fontSize = textSize,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Bold,
                    color = outlineColor
                )
            }
        }
    }
}

@Composable
fun BackStylizedButton(
    onClick: () -> Unit
){
    StylizedButton(
        text = "←", //TODO, Copiar un icono mejor para volver (estoy en medio apagón 28/04/2025 y no tengo internet por si alguien lo lee)
        onClick = onClick,
        buttonWidth = 50.dp,
        buttonHeight = 50.dp,
        size = 40.dp,
        textSize = 30.sp,
    )
}
