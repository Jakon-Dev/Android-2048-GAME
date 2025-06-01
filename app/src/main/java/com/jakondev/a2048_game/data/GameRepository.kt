package com.jakondev.a2048_game.data

import com.jakondev.a2048_game.model.GameResult
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameResultDao) {
    val allResults: Flow<List<GameResult>> = dao.getAllResults()

    suspend fun insert(result: GameResult) {
        dao.insertResult(result)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
