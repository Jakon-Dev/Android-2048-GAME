package com.example.game2048

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

// Actividad principal del juego 2048
class MainActivity : ComponentActivity() {
    // ViewModel que contiene la lógica del juego
    private val viewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Carga el composable principal
            Game2048(viewModel)
        }
    }
}


@Composable
fun Game2048(viewModel: GameViewModel) {
    val board by viewModel.board.collectAsState()
    val score by viewModel.score.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val gameBoard = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Puntuación: $score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            board.forEach { row ->
                Row {
                    row.forEach { value ->
                        Tile(value)
                    }
                }
            }

            if (!isLandscape) {
                Spacer(modifier = Modifier.height(16.dp))
                DirectionControls(
                    onUp = { viewModel.moveUp() },
                    onDown = { viewModel.moveDown() },
                    onLeft = { viewModel.moveLeft() },
                    onRight = { viewModel.moveRight() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.resetGame() }) {
                Text("Reiniciar")
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        gameBoard()

        if (isLandscape) {
            Spacer(modifier = Modifier.width(32.dp))
            DirectionControls(
                onUp = { viewModel.moveUp() },
                onDown = { viewModel.moveDown() },
                onLeft = { viewModel.moveLeft() },
                onRight = { viewModel.moveRight() }
            )
        }
    }

    if (isGameOver) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("¡Game Over!") },
            text = { Text("Tu puntuación final es $score") },
            confirmButton = {
                Button(onClick = { viewModel.resetGame() }) {
                    Text("Reintentar")
                }
            }
        )
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
        containerColor = Color(0xFF4CAF50), // Color verde llamativo
        contentColor = Color.White
    )
    val buttonShape = RoundedCornerShape(12.dp)
    val elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 6.dp,
        pressedElevation = 10.dp
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Flecha arriba
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
            // Flecha izquierda
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

            // Flecha derecha
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

        // Flecha abajo
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
    // Animación de escala (crece cuando aparece un número)
    val scale by animateFloatAsState(
        targetValue = if (value != 0) 1f else 0f,
        label = "ScaleAnimation"
    )

    // Animación del color de fondo según el valor
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
        // Transición visual entre valores (aparece/desaparece)
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "ValueTransition"
        ) { targetValue ->
            if (targetValue != 0) {
                Text(
                    text = targetValue.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
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



