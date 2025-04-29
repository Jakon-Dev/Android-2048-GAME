package com.jakondev.a2048_game.ui.game

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import rememberScreenSize

@Composable
fun GameControls(
    score: Int,
    time: Int,
    viewModel: GameViewModel,
    navController: NavController,
    isLandscape: Boolean,
) {
    val canUndo by viewModel.canGoBack.collectAsState()

    if (isLandscape) {
        val buttonWidth = 120.dp
        val buttonHeight = 50.dp

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ){
                StatsDisplay(score, formatTime(time))
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Spacer(modifier = Modifier.weight(3f))
                DirectionControls(
                    onUp = viewModel::moveUp,
                    onDown = viewModel::moveDown,
                    onLeft = viewModel::moveLeft,
                    onRight = viewModel::moveRight
                )
                StylizedButton(
                    text = "",
                    onClick = {
                        viewModel.undoMove()
                    },
                    size = 40.dp,
                    textSize = 20.sp,
                    buttonColor = if (canUndo) getPalette().surface else getPalette().error,
                    icon = {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                            contentDescription = "Undo",
                            tint = if (canUndo) getPalette().tertiary else getPalette().primary,
                        )
                    }
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
    else {
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
            StylizedButton(
                text = "",
                onClick = {
                    viewModel.undoMove()
                },
                size = 40.dp,
                textSize = 20.sp,
                buttonColor = if (canUndo) getPalette().surface else getPalette().error,
                icon = {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                        contentDescription = "Undo",
                        tint = if (canUndo) getPalette().tertiary else getPalette().primary,
                    )
                }
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
