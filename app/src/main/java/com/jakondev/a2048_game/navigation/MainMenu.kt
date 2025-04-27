package com.jakondev.a2048_game.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.Rowdies


@Composable
fun MainMenu(
    time: Int,
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
            Text(
                text = "2048",
                fontSize = 32.sp,
                fontFamily = Rowdies,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onNewGame) {
                Text(
                    text = stringResource(id = R.string.start_new_game),
                    fontFamily = Rowdies,
                )
            }

            if (time > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onResumeGame) {
                    Text(
                        text = stringResource(id = R.string.resume_game),
                        fontFamily = Rowdies,
                    )
                }
            }
        }
    }
}

