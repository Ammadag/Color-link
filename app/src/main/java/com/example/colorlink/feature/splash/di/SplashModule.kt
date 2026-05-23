package com.example.colorlink.feature.splash.di

import com.example.colorlink.feature.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}
