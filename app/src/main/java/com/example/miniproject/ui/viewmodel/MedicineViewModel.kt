package com.example.miniproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproject.data.model.Medicine
import com.example.miniproject.data.repository.MedicineRepository
import com.example.miniproject.util.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val repository: MedicineRepository,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines: StateFlow<List<Medicine>> = _medicines

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMedicines(userId: String) {
        viewModelScope.launch {
            repository.getAllMedicines(userId)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { medicines ->
                    _medicines.value = medicines
                }
        }
    }

    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch {
            try {
                val medicineId = repository.insertMedicine(medicine)
                
                // Schedule reminders for each time
                medicine.times.forEach { time ->
                    val timeInMillis = time.toSecondOfDay() * 1000L
                    reminderScheduler.scheduleReminder(medicine.userId, timeInMillis)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateMedicineStatus(medicine: Medicine, isTaken: Boolean) {
        viewModelScope.launch {
            try {
                val updatedMedicine = medicine.copy(
                    isTaken = isTaken,
                    lastTaken = if (isTaken) System.currentTimeMillis() else null
                )
                repository.updateMedicine(updatedMedicine)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            try {
                repository.deleteMedicine(medicine)
                // Cancel all reminders for this medicine
                medicine.times.forEach { time ->
                    val timeInMillis = time.toSecondOfDay() * 1000L
                    reminderScheduler.cancelReminder(medicine.userId)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 