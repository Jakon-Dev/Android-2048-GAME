package com.jakondev.a2048_game.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore(name = "user_prefs")
