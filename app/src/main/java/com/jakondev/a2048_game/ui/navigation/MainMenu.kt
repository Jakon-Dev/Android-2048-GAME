package com.jakondev.a2048_game.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.a2048_game.util.exitButton
import com.jakondev.a2048_game.viewmodel.GameViewModel


@Composable
fun MainMenu(
    time: Int,
    viewModel: GameViewModel,
    navController: NavController
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPalette().background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoButton(navController)
            Spacer(modifier = Modifier.weight(if (isLandscape) 0.5f else 1.5f))

            GameTitle()

            MainMenuButtons(
                time = time,
                viewModel = viewModel,
                navController = navController,
                isLandscape = isLandscape
            )

            Spacer(modifier = Modifier.weight(if (isLandscape) 1.8f else 1.5f))

            BottomNavigationRow(navController)

            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun InfoButton(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StylizedButton(
            text = "",
            onClick = { navController.navigate("info") },
            buttonWidth = 50.dp,
            buttonHeight = 50.dp,
            size = 40.dp,
            textSize = 20.sp,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.info),
                    tint = getPalette().tertiary
                )
            }
        )
    }
}

@Composable
fun GameTitle() {
    Text(
        text = "2048",
        fontSize = 48.sp,
        fontFamily = Rowdies,
        fontWeight = FontWeight.Bold,
        color = getPalette().tertiary,
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = "By Marc Lapeña Riu",
        fontSize = 12.sp,
        fontFamily = Rowdies,
        fontWeight = FontWeight.Light,
        color = getPalette().secondary,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun MainMenuButtons(
    time: Int,
    viewModel: GameViewModel,
    navController: NavController,
    isLandscape: Boolean
) {
    val context = LocalContext.current

    val buttonConfigs = buildList {
        add(
            StylizedButtonConfig(
                label = stringResource(id = R.string.start_new_game),
                onClick = { navController.navigate("game_settings") }
            )
        )
        if (time > 0) {
            add(
                StylizedButtonConfig(
                    label = stringResource(id = R.string.resume_game),
                    onClick = {
                        viewModel.resumeTimer()
                        navController.navigate("game")
                    }
                )
            )
        }
    }


    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttonConfigs.forEach { MenuButton(it) }
            exitButton(
                width = 200.dp,
                height = 50.dp,
                textSize = 20.sp,
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            buttonConfigs.forEach { MenuButton(it) }
            exitButton(
                width = 200.dp,
                height = 50.dp,
                textSize = 20.sp,
            )
        }
    }
}


@Composable
fun BottomNavigationRow(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttons = listOf(
            Triple(Icons.Filled.ShoppingCart, R.string.shop, "shop"),
            Triple(Icons.Filled.Star, R.string.achievements, "achievements"),
            Triple(Icons.Filled.DateRange, R.string.match_history, "history"),
            Triple(Icons.Filled.Settings, R.string.settings, "settings")
        )

        buttons.forEach { (icon, resId, route) ->
            StylizedButton(
                text = "",
                onClick = { navController.navigate(route) },
                buttonWidth = 50.dp,
                buttonHeight = 50.dp,
                size = 40.dp,
                textSize = 20.sp,
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(resId),
                        tint = getPalette().tertiary
                    )
                }
            )
        }
    }
}

@Composable
fun MenuButton(config: StylizedButtonConfig) {
    StylizedButton(
        text = config.label,
        onClick = config.onClick,
        buttonWidth = 200.dp,
        buttonHeight = 50.dp,
        size = 40.dp,
        textSize = 20.sp,
        buttonColor = config.color ?: getPalette().surface,
        textColor = config.textColor ?: getPalette().tertiary
    )
}

data class StylizedButtonConfig(
    val label: String,
    val onClick: () -> Unit,
    val color: androidx.compose.ui.graphics.Color? = null,
    val textColor: androidx.compose.ui.graphics.Color? = null
)
