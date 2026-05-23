package com.example.colorlink.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel

/**
 * Splash Route handling state collection and events.
 * Follows the pattern defined in docs/compose-ui.md.
 */
@Composable
fun SplashRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SplashEvent.NavigateToHome -> onNavigateToHome()
                SplashEvent.NavigateToOnboarding -> onNavigateToOnboarding()
            }
        }
    }

    SplashScreen()
}
