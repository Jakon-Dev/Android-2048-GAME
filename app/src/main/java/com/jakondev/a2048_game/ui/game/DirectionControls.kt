package com.jakondev.game2048.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        val buttonSize = maxWidth * 0.3f
        val spacing = maxWidth * 0.04f
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
                    size = buttonSize
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
                    size = buttonSize
                )
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton(
                    text = "↓",
                    onClick = onDown,
                    size = buttonSize
                )
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton(
                    text = "→",
                    onClick = onRight,
                    size = buttonSize,
                )
            }
        }
    }
}


