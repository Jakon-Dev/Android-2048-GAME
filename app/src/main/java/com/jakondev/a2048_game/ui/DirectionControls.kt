package com.jakondev.game2048.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DirectionControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    val buttonSize = 60.dp
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF4CAF50),
        contentColor = Color.White
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onUp, modifier = Modifier.size(buttonSize), colors = buttonColors) {
            Text("↑", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = onLeft, modifier = Modifier.size(buttonSize), colors = buttonColors) {
                Text("←", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = onRight, modifier = Modifier.size(buttonSize), colors = buttonColors) {
                Text("→", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onDown, modifier = Modifier.size(buttonSize), colors = buttonColors) {
            Text("↓", fontSize = 20.sp)
        }
    }
}
