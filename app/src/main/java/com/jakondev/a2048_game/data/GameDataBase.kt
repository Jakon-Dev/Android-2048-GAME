package com.jakondev.a2048_game.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jakondev.a2048_game.model.Achievement
import com.jakondev.a2048_game.model.GameResult

@Database(
    entities = [GameResult::class, Achievement::class],
    version = 2,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile private var INSTANCE: GameDatabase? = null

        fun getInstance(application: Application): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    application,
                    GameDatabase::class.java,
                    "game_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
