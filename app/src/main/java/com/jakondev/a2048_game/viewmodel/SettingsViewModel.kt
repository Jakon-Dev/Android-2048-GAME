package com.jakondev.a2048_game.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jakondev.a2048_game.data.UserPreferences
import com.jakondev.a2048_game.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserPreferencesRepository(application)

    private val _preferences = MutableStateFlow(UserPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences

    init {
        viewModelScope.launch {
            repo.preferencesFlow.collect { _preferences.value = it }
        }
    }

    fun toggleMute() = viewModelScope.launch {
        repo.updateMute(!_preferences.value.isMuted)
    }

    fun toggleDarkMode() = viewModelScope.launch {
        repo.updateDarkMode(!_preferences.value.isDarkMode)
    }

    fun updateUsername(name: String) {
        viewModelScope.launch {
            repo.updateUsername(name)
        }
    }

}
