package com.jakondev.a2048_game.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jakondev.a2048_game.ui.achievements.AchievementsMenu
import com.jakondev.a2048_game.ui.settings.SettingsMenu
import com.jakondev.a2048_game.ui.shop.ShopMenu
import com.jakondev.game2048.GameViewModel
import com.jakondev.game2048.ui.GameScreen

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
    }
}
