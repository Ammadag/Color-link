package com.example.colorlink.feature.onboarding

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class OnboardingViewModel(
    private val settingsRepository: SettingsRepository
) : StateViewModel<OnboardingState, OnboardingIntent, OnboardingEvent>(OnboardingState()) {

    override fun handleIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged -> {
                updateState { copy(currentPage = intent.page) }
            }
            OnboardingIntent.NextClicked -> {
                val nextPage = state.value.currentPage + 1
                if (nextPage < state.value.totalPages) {
                    updateState { copy(currentPage = nextPage) }
                } else {
                    completeOnboarding()
                }
            }
            OnboardingIntent.SkipClicked, OnboardingIntent.StartPlayingClicked -> {
                completeOnboarding()
            }
        }
    }

    private fun completeOnboarding() {
        launchSafely {
            val settings = settingsRepository.getSettings().first()
            settingsRepository.updateSettings(settings.copy(isOnboardingCompleted = true))
            emitEvent(OnboardingEvent.NavigateToHome)
        }
    }
}
