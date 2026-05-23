package com.example.colorlink.feature.settings

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.repository.SettingsRepository
import com.example.colorlink.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.flow.collectLatest

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val settingsRepository: SettingsRepository
) : StateViewModel<SettingsState, SettingsIntent, SettingsEvent>(SettingsState()) {

    override fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.LoadSettings -> loadSettings()
            is SettingsIntent.ToggleHaptics -> updateSettings { copy(hapticsEnabled = intent.enabled) }
            is SettingsIntent.ToggleMusic -> updateSettings { copy(musicEnabled = intent.enabled) }
            is SettingsIntent.ToggleSound -> updateSettings { copy(soundEnabled = intent.enabled) }
            SettingsIntent.BackClicked -> emitEvent(SettingsEvent.NavigateBack)
        }
    }

    private fun loadSettings() {
        launchSafely {
            updateState { copy(isLoading = true) }
            getSettingsUseCase().collectLatest { settings ->
                updateState { copy(isLoading = false, settings = settings) }
            }
        }
    }

    private fun updateSettings(reducer: com.example.colorlink.domain.model.Settings.() -> com.example.colorlink.domain.model.Settings) {
        launchSafely {
            val newSettings = state.value.settings.reducer()
            settingsRepository.updateSettings(newSettings)
        }
    }
}
