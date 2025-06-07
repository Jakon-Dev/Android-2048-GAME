package com.jakondev.a2048_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jakondev.a2048_game.model.GameResult
import com.jakondev.a2048_game.model.Achievement

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

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
