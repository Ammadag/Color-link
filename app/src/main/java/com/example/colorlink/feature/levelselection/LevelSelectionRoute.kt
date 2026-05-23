package com.example.colorlink.feature.levelselection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun LevelSelectionRoute(
    onNavigateToGameplay: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit,
    viewModel: LevelSelectionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(LevelSelectionIntent.LoadLevels)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LevelSelectionEvent.NavigateToGameplay -> onNavigateToGameplay(event.levelId)
                LevelSelectionEvent.NavigateBack -> onBack()
                LevelSelectionEvent.NavigateToHome -> onNavigateToHome()
                LevelSelectionEvent.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    LevelSelectionScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
