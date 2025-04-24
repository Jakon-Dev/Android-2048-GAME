package com.jakondev.a2048_game.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jakondev.game2048.GameViewModel
import com.jakondev.game2048.ui.GameScreen

@Composable
fun AppNavigation(viewModel: GameViewModel) {
    val navController = rememberNavController()
    val score = viewModel.score.collectAsState()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenu(
                score = score.value,
                onNewGame = {
                    viewModel.resetGame()
                    navController.navigate("game")
                },
                onResumeGame = {
                    navController.navigate("game")
                }
            )
        }
        composable("game") {
            GameScreen(viewModel, navController)
        }
    }
}
