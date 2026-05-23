package com.example.colorlink.feature.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val totalPages: Int = 3
)

sealed interface OnboardingIntent {
    data object SkipClicked : OnboardingIntent
    data object NextClicked : OnboardingIntent
    data object StartPlayingClicked : OnboardingIntent
    data class PageChanged(val page: Int) : OnboardingIntent
}

sealed interface OnboardingEvent {
    data object NavigateToHome : OnboardingEvent
}
