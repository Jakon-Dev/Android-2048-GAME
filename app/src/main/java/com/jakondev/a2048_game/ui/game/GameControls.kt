package com.jakondev.a2048_game.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.Rowdies
import com.jakondev.a2048_game.ui.theme.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.game2048.GameViewModel
import com.jakondev.game2048.ui.DirectionControls
import com.jakondev.game2048.ui.formatTime

@Composable
fun GameControls(
    score: Int,
    time: Int,
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
                StatsDisplay(score, formatTime(time))
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
        val buttonWidth = 120.dp
        val buttonHeight = 50.dp

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            DirectionControls(
                onUp = viewModel::moveUp,
                onDown = viewModel::moveDown,
                onLeft = viewModel::moveLeft,
                onRight = viewModel::moveRight
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight + 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(3f))
                StylizedButton(
                    text = stringResource(id = R.string.restart),
                    onClick = {
                        viewModel.resetGame()
                    },
                    buttonWidth = buttonWidth,
                    buttonHeight = buttonHeight,
                    size = 40.dp,
                    textSize = 20.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                StylizedButton(
                    text = stringResource(id = R.string.menu),
                    onClick = {
                    viewModel.pauseTimer()
                    navController.navigate("menu")},
                    buttonWidth = buttonWidth,
                    buttonHeight = buttonHeight,
                    size = 40.dp,
                    textSize = 20.sp,
                )
                Spacer(modifier = Modifier.weight(3f))
            }
        }
    }
}

@Composable
fun StatsDisplay(score: Int, time: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScoreDisplay(score)
        TimeDisplay(time)
    }
}

@Composable
fun TimeDisplay(time: String) {
    Text(
        text = stringResource(id = R.string.final_time_message, time),
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        fontFamily = Rowdies,
        color = getPalette().onBackground,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
}

@Composable
fun ScoreDisplay(score: Int) {
    Text(
        text = stringResource(id = R.string.points, score),
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        fontFamily = Rowdies,
        color = getPalette().onBackground,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
}
