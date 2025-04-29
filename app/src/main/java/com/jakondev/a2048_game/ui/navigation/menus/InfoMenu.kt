package com.jakondev.a2048_game.ui.navigation.menus

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.BackStylizedButton

@Composable
fun InfoMenu(
    navController: NavHostController,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPalette().background
    ) {
        if (isLandscape){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Botón de regreso
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackStylizedButton(
                        onClick = {
                            navController.navigate("menu")
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Título
                    Text(
                        text = stringResource(id = R.string.info),
                        fontSize = 48.sp,
                        fontFamily = Rowdies,
                        fontWeight = FontWeight.Bold,
                        color = getPalette().tertiary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TutorialStep(
                            emoji = "🎯",
                            title = stringResource(id = R.string.tutorial_objective_title),
                            description = stringResource(id = R.string.tutorial_objective_info)
                        )
                        Spacer(modifier = Modifier.weight(0.1f))
                        TutorialStep(
                            emoji = "🔄",
                            title = stringResource(id = R.string.tutorial_controles_title),
                            description = stringResource(id = R.string.tutorial_controles_info)
                        )
                        Spacer(modifier = Modifier.weight(0.1f))

                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TutorialStep(
                            emoji = "➕",
                            title = stringResource(id = R.string.tutorial_combination_title),
                            description = stringResource(id = R.string.tutorial_combination_info)
                        )
                        Spacer(modifier = Modifier.weight(0.1f))
                        TutorialStep(
                            emoji = "❌",
                            title = stringResource(id = R.string.tutorial_endgame_title),
                            description = stringResource(id = R.string.tutorial_endgame_info)
                        )

                        Spacer(modifier = Modifier.weight(2f))
                    }
                }


            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Botón de regreso
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackStylizedButton(
                        onClick = {
                            navController.navigate("menu")
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(0.2f))
                    // Título
                    Text(
                        text = stringResource(id = R.string.info),
                        fontSize = 48.sp,
                        fontFamily = Rowdies,
                        fontWeight = FontWeight.Bold,
                        color = getPalette().tertiary,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.weight(0.2f))

                    // Instrucciones
                    TutorialStep(
                        emoji = "🎯",
                        title = stringResource(id = R.string.tutorial_objective_title),
                        description = stringResource(id = R.string.tutorial_objective_info)
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                    TutorialStep(
                        emoji = "🔄",
                        title = stringResource(id = R.string.tutorial_controles_title),
                        description = stringResource(id = R.string.tutorial_controles_info)
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                    TutorialStep(
                        emoji = "➕",
                        title = stringResource(id = R.string.tutorial_combination_title),
                        description = stringResource(id = R.string.tutorial_combination_info)
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                    TutorialStep(
                        emoji = "❌",
                        title = stringResource(id = R.string.tutorial_endgame_title),
                        description = stringResource(id = R.string.tutorial_endgame_info)
                    )

                    Spacer(modifier = Modifier.weight(2f))
                }
            }
        }
    }
}

@Composable
fun TutorialStep(
    emoji: String,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = emoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Rowdies,
                color = getPalette().tertiary
            )
        }
        Text(
            text = description,
            fontSize = 16.sp,
            fontFamily = Rowdies,
            fontWeight = FontWeight.Normal,
            color = getPalette().secondary,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}
