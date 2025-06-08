package com.jakondev.a2048_game.data

import com.jakondev.a2048_game.model.Achievement
import kotlinx.coroutines.flow.Flow

class AchievementRepository(private val dao: AchievementDao) {
    val achievements: Flow<List<Achievement>> = dao.getAll()

    suspend fun unlockAchievement(id: String) {
        val current = dao.getAllAchievementsOnce().find { it.id == id } ?: return
        if (!current.isUnlocked) {
            dao.update(current.copy(isUnlocked = true))
        }
    }

    suspend fun insertDefaults(defaults: List<Achievement>) {
        val current = dao.getAllAchievementsOnce()
        val existingIds = current.map { it.id }.toSet()
        val newAchievements = defaults.filterNot { it.id in existingIds }
        if (newAchievements.isNotEmpty()) {
            dao.insertAll(newAchievements)
        }
    }
}
