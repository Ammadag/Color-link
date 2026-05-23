package com.example.colorlink.domain.repository

import com.example.colorlink.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
}
