package com.jakondev.a2048_game.data

import androidx.room.*
import com.jakondev.a2048_game.model.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun getAll(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievementsOnce(): List<Achievement>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Update
    suspend fun update(achievement: Achievement)
}
