package com.example.colorlink.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.colorlink.domain.model.Settings
import com.example.colorlink.domain.model.ThemeMode
import com.example.colorlink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val soundEnabled = booleanPreferencesKey("sound_enabled")
        val musicEnabled = booleanPreferencesKey("music_enabled")
        val hapticsEnabled = booleanPreferencesKey("haptics_enabled")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val language = stringPreferencesKey("language")
        val themeMode = stringPreferencesKey("theme_mode")
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
    }

    override fun getSettings(): Flow<Settings> = dataStore.data.map { preferences ->
        Settings(
            soundEnabled = preferences[Keys.soundEnabled] ?: true,
            musicEnabled = preferences[Keys.musicEnabled] ?: true,
            hapticsEnabled = preferences[Keys.hapticsEnabled] ?: true,
            notificationsEnabled = preferences[Keys.notificationsEnabled] ?: true,
            language = preferences[Keys.language] ?: "English",
            themeMode = ThemeMode.valueOf(preferences[Keys.themeMode] ?: ThemeMode.Dark.name),
            isOnboardingCompleted = preferences[Keys.onboardingCompleted] ?: false
        )
    }

    override suspend fun updateSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[Keys.soundEnabled] = settings.soundEnabled
            preferences[Keys.musicEnabled] = settings.musicEnabled
            preferences[Keys.hapticsEnabled] = settings.hapticsEnabled
            preferences[Keys.notificationsEnabled] = settings.notificationsEnabled
            preferences[Keys.language] = settings.language
            preferences[Keys.themeMode] = settings.themeMode.name
            preferences[Keys.onboardingCompleted] = settings.isOnboardingCompleted
        }
    }
}
