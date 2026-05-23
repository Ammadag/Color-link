package com.example.colorlink.feature.gameplay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameplayRoute(
    levelId: String,
    onBack: () -> Unit,
    onNavigateToLevelSelection: () -> Unit,
    onNavigateToGameplay: (String) -> Unit,
    viewModel: GameplayViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(levelId) {
        viewModel.handleIntent(GameplayIntent.LoadLevel(levelId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GameplayEvent.NavigateBack -> onBack()
                GameplayEvent.NavigateToLevelSelection -> onNavigateToLevelSelection()
                is GameplayEvent.NavigateToGameplay -> onNavigateToGameplay(event.levelId)
                is GameplayEvent.ShowError -> {
                    // Could show a Snackbar here
                }
                GameplayEvent.ShowLevelComplete -> {
                    // Handled via state.isCompleted in GameplayScreen
                }
                GameplayEvent.PlayWinAnimation -> {
                    // Could trigger specific animation state
                }
            }
        }
    }

    GameplayScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
