package com.example.colorlink.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.colorlink.data.repository.LevelRepositoryImpl
import com.example.colorlink.data.repository.ProgressRepositoryImpl
import com.example.colorlink.data.repository.SettingsRepositoryImpl
import com.example.colorlink.domain.repository.LevelRepository
import com.example.colorlink.domain.repository.ProgressRepository
import com.example.colorlink.domain.repository.SettingsRepository
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("colorlink_storage") }
        )
    }

    single<LevelRepository> { LevelRepositoryImpl(androidContext(), get()) }
    single<ProgressRepository> { ProgressRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}
