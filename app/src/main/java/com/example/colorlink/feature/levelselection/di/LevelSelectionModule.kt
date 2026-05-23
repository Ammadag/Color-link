package com.example.colorlink.feature.levelselection.di

import com.example.colorlink.feature.levelselection.LevelSelectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val levelSelectionModule = module {
    viewModel { LevelSelectionViewModel(get()) }
}
