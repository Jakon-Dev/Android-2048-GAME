package com.jakondev.a2048_game.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val ALIAS_KEY = stringPreferencesKey("alias")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            alias = prefs[ALIAS_KEY] ?: "Player"
        )
    }

    suspend fun setAlias(alias: String) {
        context.dataStore.edit { prefs ->
            prefs[ALIAS_KEY] = alias
        }
    }
}
