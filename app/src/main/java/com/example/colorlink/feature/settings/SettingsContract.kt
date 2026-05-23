package com.example.colorlink.feature.settings

import com.example.colorlink.domain.model.Settings

data class SettingsState(
    val settings: Settings = Settings(),
    val isLoading: Boolean = false
)

sealed interface SettingsIntent {
    data object LoadSettings : SettingsIntent
    data class ToggleSound(val enabled: Boolean) : SettingsIntent
    data class ToggleMusic(val enabled: Boolean) : SettingsIntent
    data class ToggleHaptics(val enabled: Boolean) : SettingsIntent
    data class ToggleNotifications(val enabled: Boolean) : SettingsIntent
    data class LanguageSelected(val language: String) : SettingsIntent
    data object BackClicked : SettingsIntent
    data class TabClicked(val route: String) : SettingsIntent
}

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data object NavigateToHome : SettingsEvent
    data object NavigateToLevelSelection : SettingsEvent
}
