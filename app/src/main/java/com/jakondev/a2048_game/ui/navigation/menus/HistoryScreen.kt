package com.jakondev.a2048_game.ui.navigation.menus

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.game2048.R
import com.jakondev.a2048_game.data.GameDatabase
import com.jakondev.a2048_game.data.GameRepository
import com.jakondev.a2048_game.model.GameResult
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette
import com.jakondev.a2048_game.util.BackStylizedButton
import com.jakondev.a2048_game.util.StylizedButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var gameResults by remember { mutableStateOf(listOf<GameResult>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val db = GameDatabase.getDatabase(context)
        val repo = GameRepository(db.gameResultDao())
        repo.allResults.collect { results -> gameResults = results }
    }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    BackStylizedButton { navController.navigate("menu") }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.match_history),
                    fontSize = 40.sp,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Bold,
                    color = getPalette().tertiary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (gameResults.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay partidas guardadas.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(gameResults) { result ->
                            HistoryStep(result)
                        }
                    }
                    StylizedButton(
                        text = stringResource(id = R.string.clear_history),
                        onClick = {
                            scope.launch {
                                val db = GameDatabase.getDatabase(context)
                                val repo = GameRepository(db.gameResultDao())
                                repo.clearAll()
                            }
                        },
                        buttonWidth = 220.dp,
                        buttonHeight = 50.dp,
                        textSize = 18.sp,
                        buttonColor = getPalette().surface,
                        textColor = getPalette().tertiary
                    )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    BackStylizedButton { navController.navigate("menu") }
                }

                Text(
                    text = stringResource(id = R.string.match_history),
                    fontSize = 48.sp,
                    fontFamily = Rowdies,
                    fontWeight = FontWeight.Bold,
                    color = getPalette().tertiary,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (gameResults.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay partidas guardadas.",
                            fontSize = 20.sp,
                            fontFamily = Rowdies,
                            fontWeight = FontWeight.Medium,
                            color = getPalette().secondary
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                        ) {
                        items(gameResults) { result ->
                            HistoryStep(result)
                        }
                    }

                    StylizedButton(
                        text = stringResource(id = R.string.clear_history),
                        onClick = {
                            scope.launch {
                                val db = GameDatabase.getDatabase(context)
                                val repo = GameRepository(db.gameResultDao())
                                repo.clearAll()
                            }
                        },
                        buttonWidth = 220.dp,
                        buttonHeight = 50.dp,
                        textSize = 18.sp,
                        buttonColor = getPalette().surface,
                        textColor = getPalette().tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryStep(result: GameResult) {
    val dateFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(result.date))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "📅 $dateFormatted",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Rowdies,
            color = getPalette().tertiary
        )
        Text(
            text = "🏆 " + stringResource(id = R.string.points, result.score),
            fontSize = 16.sp,
            fontFamily = Rowdies,
            color = getPalette().secondary,
            modifier = Modifier.padding(start = 32.dp)
        )
        Text(
            text = "⌛ " + stringResource(id = R.string.time_played, result.time),
            fontSize = 16.sp,
            fontFamily = Rowdies,
            color = getPalette().secondary,
            modifier = Modifier.padding(start = 32.dp)
        )
        Text(
            text = "🧠 " + stringResource(id = R.string.time_played, result.swipes),
            fontSize = 16.sp,
            fontFamily = Rowdies,
            color = getPalette().secondary,
            modifier = Modifier.padding(start = 32.dp)
        )
        Text(
            text = "🎯 " + stringResource(id = R.string.result) + " ${result.result}",
            fontSize = 16.sp,
            fontFamily = Rowdies,
            color = getPalette().secondary,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}
