package com.example.miniproject.data.repository

import com.example.miniproject.data.local.MedicineDao
import com.example.miniproject.data.model.Medicine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao
) {
    fun getAllMedicines(userId: String): Flow<List<Medicine>> {
        return medicineDao.getAllMedicines(userId)
    }

    suspend fun getMedicineById(id: Long): Medicine? {
        return medicineDao.getMedicineById(id)
    }

    suspend fun insertMedicine(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine)
    }

    suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }

    fun getMedicinesForTime(userId: String, time: String): Flow<List<Medicine>> {
        return medicineDao.getMedicinesForTime(userId, time)
    }
} 