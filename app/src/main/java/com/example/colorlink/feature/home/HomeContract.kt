package com.example.colorlink.feature.home

data class HomeState(
    val currentLevelNumber: Int = 24,
    val totalStars: Int = 0,
    val coins: Int = 1250,
    val hints: Int = 10,
    val streakDays: Int = 7,
    val unlockedWorldInfo: String = "World 3 unlocked",
    val selectedTab: Int = 0
)

sealed interface HomeIntent {
    data object PlayClicked : HomeIntent
    data object LevelSelectionClicked : HomeIntent
    data object DailyPuzzleClicked : HomeIntent
    data object SettingsClicked : HomeIntent
    data class TabClicked(val index: Int) : HomeIntent
}

sealed interface HomeEvent {
    data class NavigateToGameplay(val levelId: String) : HomeEvent
    data object NavigateToLevelSelection : HomeEvent
    data object NavigateToDailyPuzzle : HomeEvent
    data object NavigateToSettings : HomeEvent
}
