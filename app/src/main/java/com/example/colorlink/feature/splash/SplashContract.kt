package com.example.colorlink.feature.splash

data object SplashState

sealed interface SplashIntent {
    data object AnimationFinished : SplashIntent
}

sealed interface SplashEvent {
    data object NavigateToHome : SplashEvent
    data object NavigateToOnboarding : SplashEvent
}
