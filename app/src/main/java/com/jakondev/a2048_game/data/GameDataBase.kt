package com.jakondev.a2048_game.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jakondev.a2048_game.model.GameResult

@Database(entities = [GameResult::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
