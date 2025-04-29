package com.jakondev.game2048.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakondev.a2048_game.util.StylizedButton

@Composable
fun DirectionControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        var buttonSize = maxWidth * 0.3f
        var spacing = maxWidth * 0.04f
        var textSize = 50.sp

        if (isLandscape){
            buttonSize = maxWidth * 0.3f
            spacing = maxWidth * 0.04f
            textSize = 30.sp
        }

        val buttonTotalHeight = buttonSize * 1f // Aseguramos suficiente espacio para animaciones

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Flecha arriba, dentro de un Box con altura fija
            Box(
                modifier = Modifier
                    .height(buttonTotalHeight)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                StylizedButton(
                    text = "↑",
                    onClick = onUp,
                    size = buttonSize,
                    textSize = textSize
                )
            }

            // Las otras tres flechas en fila
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonTotalHeight) // También altura fija para que no se mueva
            ) {
                StylizedButton(
                    text = "←",
                    onClick = onLeft,
                    size = buttonSize,
                    textSize = textSize
                )
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton(
                    text = "↓",
                    onClick = onDown,
                    size = buttonSize,
                    textSize = textSize
                )
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton(
                    text = "→",
                    onClick = onRight,
                    size = buttonSize,
                    textSize = textSize
                )
            }
        }
    }
}


