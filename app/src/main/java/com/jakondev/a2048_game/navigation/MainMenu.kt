package com.jakondev.a2048_game.navigation

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.Rowdies
import com.jakondev.a2048_game.ui.theme.getPalette
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.game2048.GameViewModel


@Composable
fun MainMenu(
    time: Int,
    viewModel: GameViewModel,
    navController: NavController
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                StylizedButton(
                    text = "",
                    onClick = {
                        navController.navigate("info")
                              },
                    buttonWidth = 50.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(R.string.info),
                            tint = getPalette().tertiary
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1.5f))

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
                onClick = {
                    viewModel.resetGame()
                    navController.navigate("game")
                },
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
                    onClick = {
                        viewModel.resumeTimer()
                        navController.navigate("game")
                    },
                    buttonWidth = 200.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                )

            }

            if (isLandscape) {
                Spacer(modifier = Modifier.weight(1.8f))
            } else {
                Spacer(modifier = Modifier.weight(1.5f))
            }

            Row {
                Spacer(modifier = Modifier.weight(2f))

                StylizedButton(
                    text = "",
                    onClick = {
                        navController.navigate("shop")
                    },
                    buttonWidth = 50.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = stringResource(R.string.shop),
                            tint = getPalette().tertiary
                        )
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                StylizedButton(
                    text = "",
                    onClick = {
                        navController.navigate("achievements")
                    },
                    buttonWidth = 50.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.achievements),
                            tint = getPalette().tertiary
                        )
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                StylizedButton(
                    text = "",
                    onClick = {
                        navController.navigate("settings")
                    },
                    buttonWidth = 50.dp,
                    buttonHeight = 50.dp,
                    size = 40.dp,
                    textSize = 20.sp,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = getPalette().tertiary
                        )
                    }
                )

                Spacer(modifier = Modifier.weight(2f))
            }




            Spacer(modifier = Modifier.weight(0.5f))
        }

    }
}

