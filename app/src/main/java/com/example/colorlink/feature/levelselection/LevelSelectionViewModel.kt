package com.example.colorlink.feature.levelselection

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.usecase.GetLevelsWithProgressUseCase
import com.example.colorlink.domain.usecase.GetUserStatsUseCase
import com.example.colorlink.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

class LevelSelectionViewModel(
    private val getLevelsWithProgressUseCase: GetLevelsWithProgressUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase
) : StateViewModel<LevelSelectionState, LevelSelectionIntent, LevelSelectionEvent>(
    LevelSelectionState()
) {

    init {
        observeUserStats()
    }

    override fun handleIntent(intent: LevelSelectionIntent) {
        when (intent) {
            LevelSelectionIntent.LoadLevels -> loadLevels(state.value.selectedPackId)
            is LevelSelectionIntent.PackSelected -> {
                updateState { copy(selectedPackId = intent.packId) }
                loadLevels(intent.packId)
            }
            is LevelSelectionIntent.LevelClicked -> emitEvent(
                LevelSelectionEvent.NavigateToGameplay(
                    intent.levelId
                )
            )

            LevelSelectionIntent.BackClicked -> emitEvent(LevelSelectionEvent.NavigateBack)
            is LevelSelectionIntent.TabClicked -> {
                when (intent.route) {
                    Screen.Home.route -> emitEvent(LevelSelectionEvent.NavigateToHome)
                    Screen.Settings.route -> emitEvent(LevelSelectionEvent.NavigateToSettings)
                }
            }
        }
    }

    private fun loadLevels(packId: String) {
        launchSafely {
            updateState { copy(isLoading = true) }
            getLevelsWithProgressUseCase(packId).collectLatest { levels ->
                val completed = levels.count { it.progress?.isCompleted == true }
                updateState { 
                    copy(
                        isLoading = false, 
                        levels = levels,
                        completedCount = completed,
                        totalCount = levels.size
                    ) 
                }
            }
        }
    }

    private fun observeUserStats() {
        launchSafely {
            getUserStatsUseCase().collectLatest { stats ->
                updateState {
                    copy(
                        hints = stats.hints,
                        coins = stats.coins
                    )
                }
            }
        }
    }
}
