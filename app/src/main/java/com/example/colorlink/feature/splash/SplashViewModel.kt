package com.example.colorlink.feature.splash

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class SplashViewModel(
    private val settingsRepository: SettingsRepository
) : StateViewModel<SplashState, SplashIntent, SplashEvent>(SplashState) {

    init {
        startSplashTimer()
    }

    override fun handleIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.AnimationFinished -> navigateNext()
        }
    }

    private fun startSplashTimer() {
        launchSafely {
            delay(5500)
            handleIntent(SplashIntent.AnimationFinished)
        }
    }

    private fun navigateNext() {
        launchSafely {
            val settings = settingsRepository.getSettings().first()
            if (settings.isOnboardingCompleted) {
                emitEvent(SplashEvent.NavigateToHome)
            } else {
                emitEvent(SplashEvent.NavigateToOnboarding)
            }
        }
    }
}
