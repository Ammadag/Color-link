package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

class UpdateStreakUseCase(
    private val repository: UserStatsRepository
) {
    suspend operator fun invoke() {
        val stats = repository.getUserStats().first()
        val now = System.currentTimeMillis()
        val lastPlayed = stats.lastPlayedDate ?: 0L

        if (lastPlayed == 0L) {
            repository.updateStreaks(1)
            repository.updateLastPlayedDate(now)
            return
        }

        val diff = now - lastPlayed
        val daysDiff = TimeUnit.MILLISECONDS.toDays(diff)

        if (isSameDay(now, lastPlayed)) {
            // Already played today
            return
        }

        if (daysDiff == 1L) {
            repository.updateStreaks(stats.streaks + 1)
        } else {
            repository.updateStreaks(1)
        }
        repository.updateLastPlayedDate(now)
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
