package com.example.colorlink.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.colorlink.domain.model.UserStats
import com.example.colorlink.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStatsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserStatsRepository {

    private object Keys {
        val hints = intPreferencesKey("user_hints")
        val streaks = intPreferencesKey("user_streaks")
        val coins = intPreferencesKey("user_coins")
        val lastPlayedDate = longPreferencesKey("last_played_date")
    }

    override fun getUserStats(): Flow<UserStats> = dataStore.data.map { preferences ->
        UserStats(
            hints = preferences[Keys.hints] ?: 5, // Default 5 hints
            streaks = preferences[Keys.streaks] ?: 0,
            coins = preferences[Keys.coins] ?: 0,
            lastPlayedDate = preferences[Keys.lastPlayedDate]
        )
    }

    override suspend fun updateHints(hints: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.hints] = hints
        }
    }

    override suspend fun updateStreaks(streaks: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.streaks] = streaks
        }
    }

    override suspend fun updateCoins(coins: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.coins] = coins
        }
    }

    override suspend fun updateLastPlayedDate(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.lastPlayedDate] = timestamp
        }
    }
}
