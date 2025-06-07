package com.jakondev.a2048_game.ui.game

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.a2048_game.util.exitButton
import com.jakondev.a2048_game.util.rememberScreenSize
import com.jakondev.a2048_game.util.sendEmail
import com.jakondev.a2048_game.viewmodel.GameViewModel


@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val board = viewModel.board.collectAsState()
    val score = viewModel.score.collectAsState()
    val gameMode = viewModel.currentMode.collectAsState()
    val time = viewModel.time.collectAsState()
    val isGameOver = viewModel.isGameOver.collectAsState()
    val is2048 = viewModel.is2048.collectAsState()

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenSize = rememberScreenSize()
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val spacing = screenSize.width * 0.02f
    val padding = screenSize.width * 0.04f
    val boardSize = if (isLandscape) screenSize.width * 0.3f else screenSize.height * 0.35f

    val gameOverReason = viewModel.gameOverReason.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.playSound.collect { event ->
            val resId = when (event) {
                is GameViewModel.SoundEvent.Win -> R.raw.win
                is GameViewModel.SoundEvent.Lose -> R.raw.lose
                is GameViewModel.SoundEvent.Button -> R.raw.button
                is GameViewModel.SoundEvent.Pop -> R.raw.pop
            }

            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
            mediaPlayer.start()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getPalette().background)
            .padding(padding)
    ) {
        BoardLayout(
            isLandscape = isLandscape,
            board = board.value,
            score = score.value,
            time = time.value,
            boardSize = boardSize,
            spacing = spacing,
            viewModel = viewModel,
            navController = navController
        )

        if (is2048.value) {
            viewModel.pauseTimer()
            VictoryDialog(score.value, time.value, viewModel, navController)
        }


        if (isGameOver.value) {
            viewModel.pauseTimer()
            GameOverDialog(score.value, time.value, viewModel, navController, gameOverReason.value)
        }
    }
}

@Composable
private fun BoardLayout(
    isLandscape: Boolean,
    board: Array<IntArray>,
    score: Int,
    time: Int,
    boardSize: Dp,
    spacing: Dp,
    viewModel: GameViewModel,
    navController: NavController
) {
    if (isLandscape) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            GameBoard(board, width = boardSize, height = boardSize)
            GameControls(score, time, viewModel, navController, isLandscape)
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.fillMaxSize()
        ) {
            StatsDisplay(score, formatTime(time), viewModel)
            GameBoard(board, width = boardSize, height = boardSize)
            GameControls(score, time, viewModel, navController, isLandscape)
        }
    }
}

@Composable
private fun VictoryDialog(score: Int, time: Int, viewModel: GameViewModel, navController: NavController) {
    val context = LocalContext.current
    AlertDialog(
        containerColor = getPalette().background,
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.you_win),
                fontFamily = Rowdies,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(text = stringResource(R.string.win_message), fontFamily = Rowdies)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.final_points_message, score), fontFamily = Rowdies)
                Text(text = stringResource(R.string.time_played, formatTime(time)), fontFamily = Rowdies)
            }
        },
        confirmButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                StylizedButton(
                    text = stringResource(R.string.resume_game),
                    onClick = { viewModel.resumeGame() },
                    buttonWidth = 256.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                SharedDialogButtons(score, time, viewModel, navController)
            }
        }
    )
}

@Composable
private fun GameOverDialog(
    score: Int,
    time: Int,
    viewModel: GameViewModel,
    navController: NavController,
    reason: String
) {

    val gameOverMessage = when (reason) {
        "timeout" -> stringResource(R.string.game_over_timeout)
        "no_moves" -> stringResource(R.string.game_over_no_moves)
        else -> stringResource(R.string.game_over)
    }

    AlertDialog(
        containerColor = getPalette().background,
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.game_over),
                fontFamily = Rowdies,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(text = gameOverMessage, fontFamily = Rowdies, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(R.string.points, score), fontFamily = Rowdies)
                if (reason.equals("timeout")) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string.time_played, formatTime(time)), fontFamily = Rowdies)
                }
            }
        },
        confirmButton = {
            SharedDialogButtons(score, time, viewModel, navController)
        }
    )
}

@Composable
private fun SharedDialogButtons(
    score: Int,
    time: Int,
    viewModel: GameViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val username = viewModel.userPreferences.collectAsState().value.username

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StylizedButton(
                text = stringResource(R.string.restart),
                onClick = {
                    viewModel.resetGame()
                    viewModel.resumeTimer()
                },
                buttonWidth = 120.dp,
                buttonHeight = 50.dp,
                size = 40.dp,
                textSize = 18.sp
            )
            StylizedButton(
                text = stringResource(R.string.share_via_email),
                onClick = {
                    val subject = context.getString(R.string.email_subject)
                    val body = context.getString(R.string.email_body, username, score, formatTime(time))
                    sendEmail(context, subject, body)
                },
                buttonWidth = 120.dp,
                buttonHeight = 50.dp,
                size = 40.dp,
                textSize = 18.sp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StylizedButton(
                text = stringResource(R.string.menu),
                onClick = {
                    viewModel.pauseTimer()
                    navController.navigate("menu")
                },
                buttonWidth = 120.dp,
                buttonHeight = 50.dp,
                size = 40.dp,
                textSize = 18.sp
            )
            exitButton(
                width = 120.dp,
                height = 50.dp,
                textSize = 18.sp,
            )
        }
    }
}

@Composable
fun StatsDisplay(score: Int, time: String, viewModel: GameViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScoreDisplay(score)
        TimeDisplay(time, viewModel)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TimeDisplay(time: String, viewModel: GameViewModel) {
    val displayText = if (viewModel.currentMode.value == GameViewModel.GameMode.CLASSIC) {
        stringResource(id = R.string.time_played, time)
    } else {
        stringResource(id = R.string.time_remaining, time)
    }

    Text(
        text = displayText,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        fontFamily = Rowdies,
        color = getPalette().onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
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


fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
