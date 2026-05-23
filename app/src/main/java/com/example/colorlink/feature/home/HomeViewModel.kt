package com.example.colorlink.feature.home

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.usecase.GetProgressSummaryUseCase
import com.example.colorlink.domain.usecase.GetUserStatsUseCase
import com.example.colorlink.domain.usecase.UpdateStreakUseCase
import com.example.colorlink.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val getProgressSummaryUseCase: GetProgressSummaryUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase
) : StateViewModel<HomeState, HomeIntent, HomeEvent>(HomeState()) {

    init {
        loadHomeData()
        observeUserStats()
        updateStreak()
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.PlayClicked -> {
                // Navigate to the next available level
                emitEvent(HomeEvent.NavigateToGameplay("level_${state.value.currentLevelNumber}"))
            }

            HomeIntent.LevelSelectionClicked -> {
                emitEvent(HomeEvent.NavigateToLevelSelection)
            }

            HomeIntent.DailyPuzzleClicked -> {
                emitEvent(HomeEvent.NavigateToDailyPuzzle)
            }

            HomeIntent.SettingsClicked -> {
                emitEvent(HomeEvent.NavigateToSettings)
            }

            is HomeIntent.TabClicked -> {
                when (intent.route) {
                    Screen.LevelSelection.route -> emitEvent(HomeEvent.NavigateToLevelSelection)
                    Screen.DailyPuzzle.route -> emitEvent(HomeEvent.NavigateToDailyPuzzle)
                    Screen.Settings.route -> emitEvent(HomeEvent.NavigateToSettings)
                }
            }
        }
    }

    private fun loadHomeData() {
        launchSafely {
            val summary = getProgressSummaryUseCase()

            updateState {
                copy(
                    totalStars = summary.totalStars,
                    currentLevelNumber = summary.nextLevelNumber,
                    unlockedWorldInfo = "Pack: Beginner" // For now, we only have beginner pack
                )
            }
        }
    }

    private fun observeUserStats() {
        launchSafely {
            getUserStatsUseCase().collectLatest { stats ->
                updateState {
                    copy(
                        hints = stats.hints,
                        streakDays = stats.streaks,
                        coins = stats.coins
                    )
                }
            }
        }
    }

    private fun updateStreak() {
        launchSafely {
            updateStreakUseCase()
        }
    }
}
