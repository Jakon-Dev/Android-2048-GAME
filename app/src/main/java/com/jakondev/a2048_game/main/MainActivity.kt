package com.jakondev.a2048_game.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.jakondev.a2048_game.ui.navigation.AppNavigation
import com.jakondev.a2048_game.viewmodel.GameViewModel


class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(viewModel)
        }
    }
}

