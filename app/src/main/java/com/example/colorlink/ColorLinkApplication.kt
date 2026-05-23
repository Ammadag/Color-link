package com.example.colorlink

import android.app.Application
import com.example.colorlink.core.di.coreModule
import com.example.colorlink.data.di.dataModule
import com.example.colorlink.domain.di.domainModule
import com.example.colorlink.feature.gameplay.di.gameplayModule
import com.example.colorlink.feature.home.di.homeModule
import com.example.colorlink.feature.levelselection.di.levelSelectionModule
import com.example.colorlink.feature.onboarding.di.onboardingModule
import com.example.colorlink.feature.settings.di.settingsModule
import com.example.colorlink.feature.splash.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ColorLinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ColorLinkApplication)
            modules(
                coreModule,
                domainModule,
                dataModule,
                splashModule,
                onboardingModule,
                homeModule,
                levelSelectionModule,
                gameplayModule,
                settingsModule
            )
        }
    }
}
