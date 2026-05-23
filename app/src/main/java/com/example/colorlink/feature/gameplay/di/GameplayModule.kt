package com.example.colorlink.feature.gameplay.di

import com.example.colorlink.feature.gameplay.GameplayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameplayModule = module {
    viewModel {
        GameplayViewModel(
            levelRepository = get(),
            progressRepository = get(),
            startPathUseCase = get(),
            validateMoveUseCase = get(),
            checkWinConditionUseCase = get(),
            calculateStarsUseCase = get(),
            interpolateCellsUseCase = get()
        )
    }
}
