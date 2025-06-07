package com.jakondev.a2048_game.ui.navigation.menus

import StylizedSwitch
import StylizedTextField
import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.BackStylizedButton
import com.jakondev.a2048_game.util.StylizedButton
import com.jakondev.a2048_game.viewmodel.GameViewModel
import com.jakondev.a2048_game.viewmodel.SettingsViewModel
import com.jakondev.a2048_game.viewmodel.SettingsViewModelFactory


@Composable
fun SettingsMenu(
    viewModel: GameViewModel,
    navController: NavHostController,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val prefs by settingsViewModel.preferences.collectAsState()

    val currentUsername = prefs.username
    var newName by remember { mutableStateOf(currentUsername) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPalette().background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Back button
            BackStylizedButton(
                onClick = { navController.navigate("menu") }
            )

            Spacer(modifier = Modifier.weight(0.1f))

            Text(
                text = stringResource(id = R.string.settings),
                fontSize = 40.sp,
                fontFamily = Rowdies,
                fontWeight = FontWeight.Bold,
                color = getPalette().tertiary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StylizedSwitch(
                            checked = !prefs.isMuted,
                            onCheckedChange = { settingsViewModel.toggleMute() },
                            label = "Sonido"
                        )

                        StylizedSwitch(
                            checked = prefs.isDarkMode,
                            onCheckedChange = { settingsViewModel.toggleDarkMode() },
                            label = "Modo oscuro"
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StylizedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                label = stringResource(id = R.string.username),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )

                            StylizedButton(
                                text = stringResource(id = R.string.save),
                                onClick = {
                                    settingsViewModel.updateUsername(newName)
                                },
                                buttonWidth = 110.dp,
                                buttonHeight = 50.dp,
                                textSize = 20.sp
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StylizedSwitch(
                        checked = !prefs.isMuted,
                        onCheckedChange = { settingsViewModel.toggleMute() },
                        label = "Sonido"
                    )

                    StylizedSwitch(
                        checked = prefs.isDarkMode,
                        onCheckedChange = { settingsViewModel.toggleDarkMode() },
                        label = "Modo oscuro"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StylizedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = stringResource(id = R.string.username),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        StylizedButton(
                            text = stringResource(id = R.string.save),
                            onClick = {
                                settingsViewModel.updateUsername(newName)
                            },
                            buttonWidth = 110.dp,
                            buttonHeight = 50.dp,
                            textSize = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


