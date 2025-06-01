package com.jakondev.a2048_game.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val time: Int,
    val swipes: Int,
    val board: String,
    val result: String,
    val date: Long
)
