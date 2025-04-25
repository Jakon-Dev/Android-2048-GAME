package com.jakondev.game2048.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jakondev.game2048.GameViewModel
import androidx.navigation.NavController
import com.jakondev.a2048_game.ui.game.GameControls
import com.jakondev.a2048_game.ui.game.GameBoard
import rememberScreenSize

@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val board = viewModel.board.collectAsState()
    val score = viewModel.score.collectAsState()
    val swipes = viewModel.swipes.collectAsState()
    val isGameOver = viewModel.isGameOver.collectAsState()

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val screenSize = rememberScreenSize()

    val spacingSmall: Dp = screenSize.width * 0.02f
    val padding: Dp = screenSize.width * 0.04f

    val boardSide = if (screenSize.isLandscape) {
        screenSize.height.coerceAtMost(screenSize.width / 2)
    } else {
        screenSize.width.coerceAtMost(screenSize.height / 2)
    }


    // Obtenemos el tamaño disponible para el tablero
    val boardWidth = boardSide
    val boardHeight = boardSide

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(padding)
    ) {
        if (isLandscape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                GameBoard(board.value, width = boardWidth, height = boardHeight)
                GameControls(score.value, viewModel, navController)
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.fillMaxSize()
            ) {
                ScoreDisplay(score.value)
                GameBoard(board.value, width = boardWidth, height = boardHeight)
                DirectionControls(
                    onUp = viewModel::moveUp,
                    onDown = viewModel::moveDown,
                    onLeft = viewModel::moveLeft,
                    onRight = viewModel::moveRight
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingSmall)
                ) {
                    Button(onClick = viewModel::resetGame) {
                        Text("Reiniciar")
                    }
                    Button(onClick = { navController.navigate("menu") }) {
                        Text("Volver al menú")
                    }
                }
            }
        }

        if (isGameOver.value) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("¡Game Over!") },
                text = { Text("Tu puntuación final es ${score.value}") },
                confirmButton = {
                    Button(onClick = viewModel::resetGame) {
                        Text("Reintentar")
                    }
                }
            )
        }
    }
}




@Composable
fun ScoreDisplay(score: Int) {
    Text(
        text = "Puntuación: $score",
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        color = Color.White,
        modifier = Modifier
            .padding(bottom = 8.dp) // Podés hacerlo relativo si querés, pero este tamaño es pequeño
    )
}
