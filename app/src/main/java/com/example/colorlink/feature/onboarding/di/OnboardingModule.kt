package com.example.colorlink.feature.onboarding.di

import com.example.colorlink.feature.onboarding.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    viewModel { OnboardingViewModel(get()) }
}
