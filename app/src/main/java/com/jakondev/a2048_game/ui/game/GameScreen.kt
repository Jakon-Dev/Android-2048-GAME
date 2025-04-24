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
import androidx.compose.ui.unit.dp
import com.jakondev.game2048.GameViewModel
import androidx.navigation.NavController
import com.jakondev.a2048_game.ui.game.GameControls
import com.jakondev.a2048_game.ui.game.GameBoard


@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val board = viewModel.board.collectAsState()
    val score = viewModel.score.collectAsState()
    val isGameOver = viewModel.isGameOver.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(16.dp)
    ) {
        val boxMaxHeight = maxHeight
        val boxMaxWidth = maxWidth

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameBoard(board.value, boxMaxHeight)
                GameControls(score.value, viewModel, navController)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreDisplay(score.value)
                GameBoard(board.value, boxMaxWidth)
                DirectionControls(
                    onUp = viewModel::moveUp,
                    onDown = viewModel::moveDown,
                    onLeft = viewModel::moveLeft,
                    onRight = viewModel::moveRight
                )
                Row {
                    Button(onClick = viewModel::resetGame) {
                        Text("Reiniciar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { navController.navigate("menu") }) {
                        Text("Volver al menú")
                    }
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






@Composable
fun ScoreDisplay(score: Int) {
    Text(
        text = "Puntuación: $score",
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        color = Color.White,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
