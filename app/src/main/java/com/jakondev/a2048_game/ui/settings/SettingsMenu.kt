package com.jakondev.a2048_game.ui.settings

import android.content.res.Resources
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.game2048.R
import com.jakondev.a2048_game.ui.theme.Rowdies
import com.jakondev.a2048_game.ui.theme.getPalette
import com.jakondev.a2048_game.util.BackStylizedButton
import com.jakondev.game2048.GameViewModel


@Composable
fun SettingsMenu(
    viewModel: GameViewModel, //TODO -> Crear una subclase de los settings del viewModel, para solo tener que pasar este y no todo el modelo.
    navController: NavHostController,
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
                Spacer(modifier = Modifier.weight(0.1f))

                Text(
                    text = stringResource(id = R.string.settings),
                    fontSize = 48.sp,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Bold,
                    color = getPalette().tertiary,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(0.1f))

                Text(
                    text = stringResource(id = R.string.coming_soon),
                    fontSize = 24.sp,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Light,
                    color = getPalette().secondary,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(2f))


            }


        }
    }
}

