package com.example.miniproject.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.miniproject.data.repository.MedicineRepository
import com.example.miniproject.service.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@HiltWorker
class MedicineReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val medicineRepository: MedicineRepository,
    private val notificationService: NotificationService
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString("userId") ?: return Result.failure()
            val timeInMillis = inputData.getLong("timeInMillis", 0L)
            
            val currentTime = LocalTime.now()
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val currentTimeStr = currentTime.format(timeFormatter)
            
            // Get all medicines for the user
            val medicines = medicineRepository.getAllMedicines(userId).first()
            
            medicines.forEach { medicine ->
                if (!medicine.isTaken) {
                    // Check if any of the medicine times are within 15 minutes of current time
                    val medicineTime = medicine.times.find { time ->
                        val timeDiff = Math.abs(
                            currentTime.toSecondOfDay() - time.toSecondOfDay()
                        )
                        timeDiff <= 900 // 15 minutes in seconds
                    }
                    
                    if (medicineTime != null) {
                        notificationService.showNotification(medicine, medicineTime)
                    }
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
} 