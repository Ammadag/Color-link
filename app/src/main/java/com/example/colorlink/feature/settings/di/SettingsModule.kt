package com.example.colorlink.feature.settings.di

import com.example.colorlink.feature.settings.SettingsViewModel
import com.example.colorlink.feature.settings.notifications.ScheduleNotificationsUseCaseImpl
import com.example.colorlink.domain.usecase.ScheduleNotificationsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory<ScheduleNotificationsUseCase> { ScheduleNotificationsUseCaseImpl(androidContext()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}
