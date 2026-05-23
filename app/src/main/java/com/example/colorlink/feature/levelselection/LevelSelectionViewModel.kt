package com.example.colorlink.feature.levelselection

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.usecase.GetLevelsWithProgressUseCase
import kotlinx.coroutines.flow.collectLatest

class LevelSelectionViewModel(
    private val getLevelsWithProgressUseCase: GetLevelsWithProgressUseCase
) : StateViewModel<LevelSelectionState, LevelSelectionIntent, LevelSelectionEvent>(LevelSelectionState()) {

    override fun handleIntent(intent: LevelSelectionIntent) {
        when (intent) {
            LevelSelectionIntent.LoadLevels -> loadLevels()
            is LevelSelectionIntent.LevelClicked -> emitEvent(LevelSelectionEvent.NavigateToGameplay(intent.levelId))
            LevelSelectionIntent.BackClicked -> emitEvent(LevelSelectionEvent.NavigateBack)
            else -> {}
        }
    }

    private fun loadLevels() {
        launchSafely {
            updateState { copy(isLoading = true) }
            getLevelsWithProgressUseCase().collectLatest { levels ->
                updateState { copy(isLoading = false, levels = levels) }
            }
        }
    }
}
