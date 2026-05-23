package com.example.colorlink.domain.model

data class Settings(
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.Dark,
    val isOnboardingCompleted: Boolean = false
)

enum class ThemeMode {
    Light, Dark, System
}
