package com.jakondev.a2048_game.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.jakondev.a2048_game.ui.navigation.AppNavigation
import com.jakondev.a2048_game.viewmodel.GameViewModel
import com.jakondev.a2048_game.viewmodel.GameViewModelFactory


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            GameViewModelFactory(application)
        ).get(GameViewModel::class.java)

        setContent {
            AppNavigation(viewModel)
        }
    }
}


