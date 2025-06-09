package com.example.miniproject.util

import android.content.Context
import androidx.work.*
import com.example.miniproject.work.MedicineReminderWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    private val context: Context
) {
    fun scheduleReminder(userId: String, timeInMillis: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MedicineReminderWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setInputData(workDataOf(
                "userId" to userId,
                "timeInMillis" to timeInMillis
            ))
            .setConstraints(constraints)
            .setInitialDelay(calculateDelay(timeInMillis), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "medicine_reminder_${userId}_${timeInMillis}",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

    fun cancelReminder(userId: String) {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("medicine_reminder_${userId}")
    }

    private fun calculateDelay(targetTimeInMillis: Long): Long {
        val currentTimeInMillis = System.currentTimeMillis()
        return if (targetTimeInMillis > currentTimeInMillis) {
            targetTimeInMillis - currentTimeInMillis
        } else {
            // If the target time has passed, schedule for the next day
            targetTimeInMillis + (24 * 60 * 60 * 1000) - currentTimeInMillis
        }
    }
} 