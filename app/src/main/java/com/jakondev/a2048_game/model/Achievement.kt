package com.jakondev.a2048_game.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val titleResId: Int,
    val descriptionResId: Int,
    val isUnlocked: Boolean = false
)
