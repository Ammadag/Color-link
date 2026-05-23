package com.example.colorlink.feature.gameplay

import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.DotColor
import com.example.colorlink.domain.model.Level

data class GameplayState(
    val isLoading: Boolean = false,
    val level: Level? = null,
    val paths: List<ColorPath> = emptyList(),
    val activePath: ColorPath? = null,
    val moveCount: Int = 0,
    val hintCount: Int = 3,
    val isPaused: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null,
    val invalidMoveMessage: String? = null
)

sealed interface GameplayIntent {
    data class LoadLevel(val levelId: String) : GameplayIntent
    data class StartPath(val position: BoardPosition) : GameplayIntent
    data class DragTo(val position: BoardPosition) : GameplayIntent
    data object EndPath : GameplayIntent
    data object Undo : GameplayIntent
    data object Restart : GameplayIntent
    data object UseHint : GameplayIntent
    data object PauseClicked : GameplayIntent
    data object ResumeClicked : GameplayIntent
    data object BackClicked : GameplayIntent
    data object LevelCompleteContinueClicked : GameplayIntent
}

sealed interface GameplayEvent {
    data object NavigateBack : GameplayEvent
    data object NavigateToNextLevel : GameplayEvent
    data class ShowError(val message: String) : GameplayEvent
    data object ShowLevelComplete : GameplayEvent
    data object PlayWinAnimation : GameplayEvent
}
