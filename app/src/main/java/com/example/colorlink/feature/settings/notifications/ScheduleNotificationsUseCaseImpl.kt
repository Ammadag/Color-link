package com.example.colorlink.feature.settings.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.colorlink.domain.usecase.ScheduleNotificationsUseCase
import java.util.concurrent.TimeUnit

class ScheduleNotificationsUseCaseImpl(
    private val context: Context
) : ScheduleNotificationsUseCase {

    override fun invoke(enabled: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (enabled) {
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(24, TimeUnit.HOURS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "daily_reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        } else {
            workManager.cancelUniqueWork("daily_reminder")
        }
    }
}
