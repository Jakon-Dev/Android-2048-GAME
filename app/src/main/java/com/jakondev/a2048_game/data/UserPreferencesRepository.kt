package com.jakondev.a2048_game.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val IS_MUTED = booleanPreferencesKey("is_muted")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USERNAME = stringPreferencesKey("username")
    }

    val preferencesFlow: Flow<UserPreferences> = context.userDataStore.data.map { prefs ->
        UserPreferences(
            isMuted = prefs[IS_MUTED] ?: false,
            isDarkMode = prefs[DARK_MODE] ?: false,
            username = prefs[USERNAME] ?: "Player"
        )
    }

    suspend fun updateMute(isMuted: Boolean) {
        context.userDataStore.edit { it[IS_MUTED] = isMuted }
    }

    suspend fun updateDarkMode(enabled: Boolean) {
        context.userDataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun updateUsername(newName: String) {
        context.userDataStore.edit { it[USERNAME] = newName }
    }
}
