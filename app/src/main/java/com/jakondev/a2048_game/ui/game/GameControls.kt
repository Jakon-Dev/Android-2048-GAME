package com.jakondev.a2048_game.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jakondev.game2048.GameViewModel
import com.jakondev.game2048.ui.DirectionControls
import com.jakondev.game2048.ui.ScoreDisplay

@Composable
fun GameControls(score: Int, viewModel: GameViewModel, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ScoreDisplay(score)
        DirectionControls(
            onUp = viewModel::moveUp,
            onDown = viewModel::moveDown,
            onLeft = viewModel::moveLeft,
            onRight = viewModel::moveRight
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = viewModel::resetGame) {
            Text("Reiniciar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("menu") }) {
            Text("Volver al menú")
        }
    }
}