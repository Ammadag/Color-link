package com.example.colorlink.feature.levelselection

import com.example.colorlink.domain.model.LevelWithProgress

data class LevelSelectionState(
    val isLoading: Boolean = false,
    val levels: List<LevelWithProgress> = emptyList(),
    val coins: Int = 1250,
    val hints: Int = 10,
    val errorMessage: String? = null
)

sealed interface LevelSelectionIntent {
    data object LoadLevels : LevelSelectionIntent
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
