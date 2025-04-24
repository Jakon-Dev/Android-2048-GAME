package com.jakondev.a2048_game.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenu(
    score: Int,
    onNewGame: () -> Unit,
    onResumeGame: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("2048 Game", fontSize = 32.sp, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onNewGame) {
                Text("Start New Game")
            }

            if (score > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onResumeGame) {
                    Text("Resume Game")
                }
            }
        }
    }
}

