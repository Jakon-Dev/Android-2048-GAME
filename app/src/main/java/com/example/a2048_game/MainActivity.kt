// MainActivity.kt
package com.example.game2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Game2048(viewModel)
        }
    }
}

@Composable
fun Game2048(viewModel: GameViewModel) {
    val board by viewModel.board.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        board.forEach { row ->
            Row {
                row.forEach { value ->
                    Tile(value)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DirectionControls(
            onUp = { viewModel.moveUp() },
            onDown = { viewModel.moveDown() },
            onLeft = { viewModel.moveLeft() },
            onRight = { viewModel.moveRight() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.resetGame() }) {
            Text("Reiniciar")
        }
    }
}

@Composable
fun DirectionControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    val buttonSize = 60.dp
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF4CAF50), // verde bonito
        contentColor = Color.White
    )
    val buttonShape = RoundedCornerShape(12.dp)
    val elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 6.dp,
        pressedElevation = 10.dp
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onUp,
            modifier = Modifier.size(buttonSize),
            colors = buttonColors,
            shape = buttonShape,
            elevation = elevation
        ) {
            Text("↑", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onLeft,
                modifier = Modifier.size(buttonSize),
                colors = buttonColors,
                shape = buttonShape,
                elevation = elevation
            ) {
                Text("←", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onRight,
                modifier = Modifier.size(buttonSize),
                colors = buttonColors,
                shape = buttonShape,
                elevation = elevation
            ) {
                Text("→", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onDown,
            modifier = Modifier.size(buttonSize),
            colors = buttonColors,
            shape = buttonShape,
            elevation = elevation
        ) {
            Text("↓", fontSize = 20.sp)
        }
    }
}




@Composable
fun Tile(value: Int) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp)
            .background(
                color = getTileColor(value),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

fun getTileColor(value: Int): Color {
    return when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color.Black
    }
}
