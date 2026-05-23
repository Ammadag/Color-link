package com.example.colorlink.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onNavigateToGameplay: (String) -> Unit,
    onNavigateToLevelSelection: () -> Unit,
    onNavigateToDailyPuzzle: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToGameplay -> onNavigateToGameplay(event.levelId)
                HomeEvent.NavigateToLevelSelection -> onNavigateToLevelSelection()
                HomeEvent.NavigateToDailyPuzzle -> onNavigateToDailyPuzzle()
                HomeEvent.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    HomeScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
