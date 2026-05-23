package com.example.colorlink.domain.di

import com.example.colorlink.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory { ValidateMoveUseCase() }
    factory { CheckWinConditionUseCase() }
    factory { StartPathUseCase() }
    factory { CalculateStarsUseCase() }
    factory { InterpolateCellsUseCase() }
    factory { GetSettingsUseCase(get()) }
    factory { GetLevelsWithProgressUseCase(get(), get()) }
}
