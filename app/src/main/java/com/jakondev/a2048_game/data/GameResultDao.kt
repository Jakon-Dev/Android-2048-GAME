package com.jakondev.a2048_game.data

import androidx.room.*
import com.jakondev.a2048_game.model.GameResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert
    suspend fun insertResult(result: GameResult)

    @Query("SELECT * FROM game_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<GameResult>>

    @Query("DELETE FROM game_results")
    suspend fun clearAll()
}
