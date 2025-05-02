package com.jakondev.a2048_game.ui.game

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.a2048_game.viewmodel.GameViewModel

@Composable
fun GameControls(
    score: Int,
    time: Int,
    viewModel: GameViewModel,
    navController: NavController,
    isLandscape: Boolean,
) {
    val canUndo by viewModel.canGoBack.collectAsState()
    val formattedTime = formatTime(time)

    if (isLandscape) {
        GameControlsLandscape(score, formattedTime, viewModel, navController, canUndo)
    } else {
        GameControlsPortrait(score, formattedTime, viewModel, navController, canUndo)
    }
}

@Composable
private fun GameControlsLandscape(
    score: Int,
    time: String,
    viewModel: GameViewModel,
    navController: NavController,
    canUndo: Boolean
) {
    val buttonWidth = 120.dp
    val buttonHeight = 50.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            StatsSection(score, time)
        }

        Spacer(modifier = Modifier.weight(0.1f))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Spacer(modifier = Modifier.weight(3f))
            DirectionControls(
                onUp = viewModel::moveUp,
                onDown = viewModel::moveDown,
                onLeft = viewModel::moveLeft,
                onRight = viewModel::moveRight
            )
            UndoButtonSection(canUndo = canUndo) { viewModel.undoMove() }
            GameButtonsSection(viewModel, navController, buttonWidth, buttonHeight)
        }
    }
}

@Composable
private fun GameControlsPortrait(
    score: Int,
    time: String,
    viewModel: GameViewModel,
    navController: NavController,
    canUndo: Boolean
) {
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
        UndoButtonSection(canUndo = canUndo) { viewModel.undoMove() }
        GameButtonsSection(viewModel, navController, buttonWidth, buttonHeight)
    }
}

@Composable
private fun StatsSection(score: Int, time: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.points, score),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontFamily = Rowdies,
            color = getPalette().onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.time_played, time),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontFamily = Rowdies,
            color = getPalette().onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun UndoButtonSection(canUndo: Boolean, onUndo: () -> Unit) {
    StylizedButton(
        text = "",
        onClick = onUndo,
        size = 40.dp,
        textSize = 20.sp,
        buttonColor = if (canUndo) getPalette().surface else getPalette().error,
        icon = {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Undo",
                tint = if (canUndo) getPalette().tertiary else getPalette().primary,
            )
        }
    )
}

@Composable
private fun GameButtonsSection(
    viewModel: GameViewModel,
    navController: NavController,
    buttonWidth: Dp,
    buttonHeight: Dp
) {
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
            onClick = { viewModel.resetGame() },
            buttonWidth = buttonWidth,
            buttonHeight = buttonHeight,
            size = 40.dp,
            textSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        StylizedButton(
            text = stringResource(id = R.string.menu),
            onClick = {
                viewModel.pauseTimer()
                navController.navigate("menu")
            },
            buttonWidth = buttonWidth,
            buttonHeight = buttonHeight,
            size = 40.dp,
            textSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Composable
fun DirectionControls(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        val buttonSize = maxWidth * 0.3f
        val spacing = maxWidth * 0.04f
        val textSize = if (isLandscape) 30.sp else 50.sp
        val buttonHeight = buttonSize

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(buttonHeight)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                StylizedButton("↑", onUp, size = buttonSize, textSize = textSize)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
            ) {
                StylizedButton("←", onLeft, size = buttonSize, textSize = textSize)
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton("↓", onDown, size = buttonSize, textSize = textSize)
                Spacer(modifier = Modifier.width(spacing))
                StylizedButton("→", onRight, size = buttonSize, textSize = textSize)
            }
        }
    }
}
