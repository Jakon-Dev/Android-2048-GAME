package com.jakondev.a2048_game.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.game2048.GameViewModel
import com.jakondev.game2048.ui.DirectionControls
import com.jakondev.game2048.ui.ScoreDisplay

@Composable
fun GameControls(
    score: Int,
    viewModel: GameViewModel,
    navController: NavController,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoreDisplay(score)
                Button(onClick = viewModel::resetGame) {
                    Text(text = stringResource(id = R.string.restart))
                }
                Button(onClick = { navController.navigate("menu") }) {
                    Text(text = stringResource(id = R.string.menu))
                }
            }
            DirectionControls(
                onUp = viewModel::moveUp,
                onDown = viewModel::moveDown,
                onLeft = viewModel::moveLeft,
                onRight = viewModel::moveRight
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DirectionControls(
                onUp = viewModel::moveUp,
                onDown = viewModel::moveDown,
                onLeft = viewModel::moveLeft,
                onRight = viewModel::moveRight
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = viewModel::resetGame) {
                    Text(text = stringResource(id = R.string.restart))
                }
                Button(onClick = { navController.navigate("menu") }) {
                    Text(text = stringResource(id = R.string.menu))
                }
            }
        }
    }
}

