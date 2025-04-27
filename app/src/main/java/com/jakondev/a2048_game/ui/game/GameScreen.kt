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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakondev.game2048.GameViewModel
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.game.GameControls
import com.jakondev.a2048_game.ui.game.GameBoard
import com.jakondev.a2048_game.ui.game.StatsDisplay
import com.jakondev.a2048_game.ui.theme.Rowdies
import com.jakondev.a2048_game.ui.theme.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import rememberScreenSize



@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val board = viewModel.board.collectAsState()
    val score = viewModel.score.collectAsState()
    val swipes = viewModel.swipes.collectAsState()
    val isGameOver = viewModel.isGameOver.collectAsState()
    val time = viewModel.time.collectAsState()


    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val screenSize = rememberScreenSize()

    val spacingSmall: Dp = screenSize.width * 0.02f
    val padding: Dp = screenSize.width * 0.04f

    val boardSide = if (screenSize.isLandscape) {
        screenSize.height.coerceAtMost(screenSize.width / 2)
    } else {
        screenSize.width.coerceAtMost(screenSize.height / 2)
    }


    val boardWidth = boardSide
    val boardHeight = boardSide

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getPalette().background)
            .padding(padding)
    ) {
        if (isLandscape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                GameBoard(board.value, width = boardWidth, height = boardHeight)
                GameControls(score.value, time.value, viewModel, navController, true)
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.fillMaxSize()
            ) {
                StatsDisplay(score.value, formatTime(time.value))
                GameBoard(board.value, width = boardWidth, height = boardHeight)
                GameControls(score.value, time.value, viewModel, navController, false)
            }
        }

        if (isGameOver.value) {
            viewModel.pauseTimer()
            AlertDialog(
                containerColor = getPalette().background,
                onDismissRequest = {},
                title = {
                    Text(
                        text = stringResource(id = R.string.game_over),
                        fontFamily = Rowdies,
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.final_points_message, score.value),
                            fontFamily = Rowdies,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.final_time_message, formatTime(time.value)),
                            fontFamily = Rowdies,
                        )
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        StylizedButton(
                            text = stringResource(id = R.string.retry),
                            onClick = {
                                viewModel.resetGame()
                                viewModel.resumeTimer()
                            },
                            buttonWidth = 120.dp,
                            buttonHeight = 50.dp,
                            size = 40.dp,
                            textSize = 20.sp,
                        )
                        StylizedButton(
                            text = stringResource(id = R.string.menu),
                            onClick = {
                                viewModel.pauseTimer()
                                navController.navigate("menu")
                            },
                            buttonWidth = 120.dp,
                            buttonHeight = 50.dp,
                            size = 40.dp,
                            textSize = 20.sp,
                        )
                    }
                }
            )

        }

    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


