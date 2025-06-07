package com.jakondev.a2048_game.ui.navigation.menus

import com.jakondev.a2048_game.model.Achievement
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.jakondev.a2048_game.viewmodel.GameViewModel


@Composable
fun AchievementsMenu(
    viewModel: GameViewModel,
    navController: NavHostController,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val achievements by viewModel.achievements.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPalette().background
    ) {
        if (isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón de regreso
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    BackStylizedButton { navController.navigate("menu") }
                }

                Spacer(modifier = Modifier.weight(0.2f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.achievements),
                        fontSize = 40.sp,
                        fontFamily = Rowdies,
                        fontWeight = FontWeight.Bold,
                        color = getPalette().tertiary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.weight(0.1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(achievements) { achievement ->
                            AchievementStep(achievement)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    BackStylizedButton { navController.navigate("menu") }
                }

                Spacer(modifier = Modifier.weight(0.2f))

                Text(
                    text = stringResource(id = R.string.achievements),
                    fontSize = 48.sp,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Bold,
                    color = getPalette().tertiary,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(0.2f))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(achievements) { achievement ->
                        AchievementStep(achievement)
                    }
                }

                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}

@Composable
fun AchievementStep(achievement: Achievement) {
    val palette = getPalette()
    val isUnlocked = achievement.isUnlocked

    val titleColor = if (isUnlocked) palette.onSurfaceVariant else palette.tertiary
    val descColor = if (isUnlocked) palette.onSurfaceVariant.copy(alpha = 0.5f) else palette.secondary
    val icon = if (isUnlocked) "✅" else "🔒"

    val title = stringResource(id = achievement.titleResId)
    val desc = stringResource(id = achievement.descriptionResId)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = icon,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Rowdies,
                color = titleColor
            )
        }
        Text(
            text = desc,
            fontSize = 16.sp,
            fontFamily = Rowdies,
            fontWeight = FontWeight.Normal,
            color = descColor,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}



