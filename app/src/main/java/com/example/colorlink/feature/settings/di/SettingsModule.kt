package com.example.colorlink.feature.settings.di

import com.example.colorlink.feature.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel(get(), get()) }
}
