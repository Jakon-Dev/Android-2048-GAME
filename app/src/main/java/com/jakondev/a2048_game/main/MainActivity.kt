package com.jakondev.a2048_game.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jakondev.a2048_game.ui.navigation.AppNavigation
import com.jakondev.a2048_game.ui.theme.main.AppTheme
import com.jakondev.a2048_game.viewmodel.GameViewModel
import com.jakondev.a2048_game.viewmodel.GameViewModelFactory
import com.jakondev.a2048_game.viewmodel.SettingsViewModel
import com.jakondev.a2048_game.viewmodel.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameViewModel = ViewModelProvider(
            this,
            GameViewModelFactory(application)
        ).get(GameViewModel::class.java)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(application)
            )
            val isDarkMode by settingsViewModel.preferences.collectAsState()

            AppTheme(isDarkMode = isDarkMode.isDarkMode) {
                AppNavigation(gameViewModel)
            }
        }
    }
}
