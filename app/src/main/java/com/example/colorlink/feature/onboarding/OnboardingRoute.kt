package com.example.colorlink.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Onboarding Route handling state collection and events.
 * Follows the pattern defined in docs/compose-ui.md.
 */
@Composable
fun OnboardingRoute(
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    OnboardingScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
