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


@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val board = viewModel.board.collectAsState()
    val score = viewModel.score.collectAsState()
    val isGameOver = viewModel.isGameOver.collectAsState()

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ScoreDisplay(score.value)

            board.value.forEach { row ->
                Row {
                    row.forEach { value ->
                        Tile(value)
                    }
                }
            }

            if (!isLandscape) {
                Spacer(modifier = Modifier.height(16.dp))
                DirectionControls(
                    onUp = viewModel::moveUp,
                    onDown = viewModel::moveDown,
                    onLeft = viewModel::moveLeft,
                    onRight = viewModel::moveRight
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = viewModel::resetGame) {
                Text("Reiniciar")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("menu") }) {
                Text("Volver al menú")
            }
        }

        if (isLandscape) {
            Spacer(modifier = Modifier.width(32.dp))
            DirectionControls(
                onUp = viewModel::moveUp,
                onDown = viewModel::moveDown,
                onLeft = viewModel::moveLeft,
                onRight = viewModel::moveRight
            )
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
