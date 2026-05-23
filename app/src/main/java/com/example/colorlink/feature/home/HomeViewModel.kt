package com.example.colorlink.feature.home

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.repository.ProgressRepository

class HomeViewModel(
    private val progressRepository: ProgressRepository
) : StateViewModel<HomeState, HomeIntent, HomeEvent>(HomeState()) {

    init {
        loadHomeData()
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
                updateState { copy(selectedTab = intent.index) }
                when (intent.index) {
                    1 -> emitEvent(HomeEvent.NavigateToLevelSelection)
                    2 -> emitEvent(HomeEvent.NavigateToDailyPuzzle)
                    3 -> emitEvent(HomeEvent.NavigateToSettings)
                }
            }
        }
    }

    private fun loadHomeData() {
        launchSafely {
            val allProgress = progressRepository.getAllProgress()
            val totalStars = allProgress.sumOf { it.stars }
            val nextLevel = (allProgress.maxOfOrNull { it.levelId.filter { char -> char.isDigit() }.toIntOrNull() ?: 0 } ?: 0) + 1
            
            updateState { 
                copy(
                    totalStars = totalStars,
                    currentLevelNumber = if (nextLevel == 0) 1 else nextLevel
                )
            }
        }
    }
}
