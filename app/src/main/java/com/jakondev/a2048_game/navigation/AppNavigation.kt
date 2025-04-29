package com.jakondev.a2048_game.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
                onNewGame = {
                    viewModel.resetGame()
                    navController.navigate("game")
                },
                onResumeGame = {
                    viewModel.resumeTimer()
                    navController.navigate("game")
                },
                onShop = {
                    navController.navigate("shop")
                },
                onAchievements = {
                    navController.navigate("achievements")
                },
                onSettings = {
                    navController.navigate("settings")
                }
            )
        }
        composable("game") {
            GameScreen(viewModel, navController)
        }
        composable("shop"){
            ShopMenu(
                viewModel = viewModel,
                onMenu = {
                    navController.navigate("menu")
                }
            )
        }
        composable("achievements"){
            AchievementsMenu(
                viewModel = viewModel,
                onMenu = {
                    navController.navigate("menu")
                }
            )
        }
        composable("settings"){
            SettingsMenu(
                viewModel = viewModel,
                onMenu = {
                    navController.navigate("menu")
                }
            )
        }
    }
}
