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
    onNavigateToNextLevel: () -> Unit,
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
                GameplayEvent.NavigateToNextLevel -> onNavigateToNextLevel()
                is GameplayEvent.ShowError -> {
                    // Could show a Snackbar here
                }
                GameplayEvent.ShowLevelComplete -> {
                    // Handled via state.isCompleted in GameplayScreen for now
                }
                GameplayEvent.PlayWinAnimation -> {
                    // Could trigger specific animation state if needed
                }
            }
        }
    }

    GameplayScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
