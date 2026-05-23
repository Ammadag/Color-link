package com.example.colorlink.domain.usecase

interface ScheduleNotificationsUseCase {
    operator fun invoke(enabled: Boolean)
}
