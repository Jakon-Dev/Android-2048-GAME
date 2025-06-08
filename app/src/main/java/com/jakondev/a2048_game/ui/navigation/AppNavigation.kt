package com.jakondev.a2048_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jakondev.a2048_game.ui.game.GameScreen
import com.jakondev.a2048_game.ui.navigation.menus.AchievementsMenu
import com.jakondev.a2048_game.ui.navigation.menus.SettingsMenu
import com.jakondev.a2048_game.ui.navigation.menus.ShopMenu
import com.jakondev.a2048_game.viewmodel.GameViewModel
import com.jakondev.a2048_game.ui.navigation.menus.GameSettingsMenu
import com.jakondev.a2048_game.ui.navigation.menus.HistoryScreen
import com.jakondev.a2048_game.ui.navigation.menus.InfoMenu

@Composable
fun AppNavigation(viewModel: GameViewModel) {
    val navController = rememberNavController()
    val time = viewModel.time.collectAsState()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenu(
                time = time.value,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("game_settings") {
            GameSettingsMenu(viewModel = viewModel, navController = navController)
        }
        composable("game") {
            GameScreen(viewModel = viewModel, navController = navController)
        }
        composable("shop") {
            ShopMenu(viewModel = viewModel, navController = navController)
        }
        composable("achievements") {
            AchievementsMenu(viewModel = viewModel, navController = navController)
        }
        composable("settings") {
            SettingsMenu(viewModel = viewModel, navController = navController)
        }
        composable("info") {
            InfoMenu(navController = navController)
        }
        composable("history") {
            HistoryScreen(navController = navController)
        }
    }
}
