package com.example.colorlink.feature.levelselection

import com.example.colorlink.domain.model.LevelWithProgress

data class LevelSelectionState(
    val isLoading: Boolean = false,
    val levels: List<LevelWithProgress> = emptyList(),
    val coins: Int = 0,
    val hints: Int = 0,
    val selectedPackId: String = "classic",
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val errorMessage: String? = null
)

sealed interface LevelSelectionIntent {
    data object LoadLevels : LevelSelectionIntent
    data class PackSelected(val packId: String) : LevelSelectionIntent
    data class LevelClicked(val levelId: String) : LevelSelectionIntent
    data object BackClicked : LevelSelectionIntent
    data class TabClicked(val route: String) : LevelSelectionIntent
}

sealed interface LevelSelectionEvent {
    data class NavigateToGameplay(val levelId: String) : LevelSelectionEvent
    data object NavigateBack : LevelSelectionEvent
    data object NavigateToHome : LevelSelectionEvent
    data object NavigateToSettings : LevelSelectionEvent
}
