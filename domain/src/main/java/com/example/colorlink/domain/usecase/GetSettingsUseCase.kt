package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.Settings
import com.example.colorlink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Settings> = repository.getSettings()
}
