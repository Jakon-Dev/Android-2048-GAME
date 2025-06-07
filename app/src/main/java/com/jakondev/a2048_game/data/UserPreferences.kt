package com.jakondev.a2048_game.data

data class UserPreferences(
    val isMuted: Boolean = false,
    val isDarkMode: Boolean = false,
    val username: String = "Player"
)

