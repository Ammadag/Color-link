package com.example.colorlink.feature.home

data class HomeState(
    val currentLevelNumber: Int = 1,
    val totalStars: Int = 0,
    val coins: Int = 0,
    val hints: Int = 0,
    val streakDays: Int = 0,
    val unlockedWorldInfo: String = "Welcome to Color Link",
    val selectedTab: Int = 0
)

sealed interface HomeIntent {
    data object PlayClicked : HomeIntent
    data object LevelSelectionClicked : HomeIntent
    data object DailyPuzzleClicked : HomeIntent
    data object SettingsClicked : HomeIntent
    data class TabClicked(val route: String) : HomeIntent
}

sealed interface HomeEvent {
    data class NavigateToGameplay(val levelId: String) : HomeEvent
    data object NavigateToLevelSelection : HomeEvent
    data object NavigateToDailyPuzzle : HomeEvent
    data object NavigateToSettings : HomeEvent
}
