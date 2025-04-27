package com.jakondev.a2048_game.navigation

import android.content.res.Resources
import androidx.compose.foundation.background
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
import com.jakondev.a2048_game.ui.theme.getPalette
import com.jakondev.a2048_game.util.StylizedButton


@Composable
fun MainMenu(
    time: Int,
    onNewGame: () -> Unit,
    onResumeGame: () -> Unit
) {
    val isLandscape = Resources.getSystem().configuration.orientation == 0

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPalette().background
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

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

            if (isLandscape) {
                Spacer(modifier = Modifier.weight(0.5f))
            } else {
                Spacer(modifier = Modifier.weight(0.2f))
            }


            StylizedButton(
                text = stringResource(id = R.string.start_new_game),
                onClick = onNewGame,
                buttonWidth = 200.dp,
                buttonHeight = 50.dp,
                size = 40.dp,
                textSize = 20.sp,
            )


            if (time > 0) {
                if (isLandscape) {
                    Spacer(modifier = Modifier.weight(0.3f))
                } else {
                    Spacer(modifier = Modifier.weight(0.05f))
                }

                StylizedButton(
                    text = stringResource(id = R.string.resume_game),
                    onClick = onResumeGame,
                    buttonWidth = 200.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                    buttonColor = getPalette().secondary,
                )

            }

            Spacer(modifier = Modifier.weight(2f))
        }

    }
}

