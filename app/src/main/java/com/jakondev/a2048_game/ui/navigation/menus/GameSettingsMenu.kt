package com.jakondev.a2048_game.ui.navigation.menus

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.BackStylizedButton
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.a2048_game.viewmodel.GameViewModel


@Composable
fun GameSettingsMenu(
    viewModel: GameViewModel,
    navController: NavHostController,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val palette = getPalette()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = palette.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(navController)

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.3f))

                GameModeSelection(viewModel, navController, isLandscape)

                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}


@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackStylizedButton(onClick = { navController.navigate("menu") })
    }

    Text(
        text = stringResource(id = R.string.gamemodes),
        fontSize = 40.sp,
        fontFamily = Rowdies,
        fontWeight = FontWeight.Bold,
        color = getPalette().tertiary,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun GameModeSelection(
    viewModel: GameViewModel,
    navController: NavHostController,
    isLandscape: Boolean
) {
    val gameModes = listOf(
        GameViewModel.GameMode.CLASSIC to R.string.classic_mode,
        GameViewModel.GameMode.COUNTDOWN_15 to R.string.mode_15_min,
        GameViewModel.GameMode.COUNTDOWN_20 to R.string.mode_20_min,
        GameViewModel.GameMode.COUNTDOWN_30 to R.string.mode_30_min,
    )

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            gameModes.chunked(2).forEach { columnModes ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    columnModes.forEach { (mode, labelId) ->
                        GameModeButton(mode, labelId, viewModel, navController)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            gameModes.forEachIndexed { index, (mode, labelId) ->
                GameModeButton(mode, labelId, viewModel, navController)
                if (index != gameModes.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun GameModeButton(
    mode: GameViewModel.GameMode,
    labelId: Int,
    viewModel: GameViewModel,
    navController: NavHostController
) {
    StylizedButton(
        text = stringResource(id = labelId),
        buttonWidth = 200.dp,
        buttonHeight = 50.dp,
        size = 40.dp,
        textSize = 20.sp,
        onClick = {
            viewModel.setGameMode(mode)
            viewModel.resetGame()
            navController.navigate("game")
        }
    )
}
