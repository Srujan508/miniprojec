package com.example.miniproject.data.local

import androidx.room.*
import com.example.miniproject.data.model.Medicine
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE userId = :userId")
    fun getAllMedicines(userId: String): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Long): Medicine?

    @Query("SELECT * FROM medicines WHERE userId = :userId AND :currentTime IN (SELECT value FROM json_each(times))")
    fun getMedicinesForTime(userId: String, currentTime: String): Flow<List<Medicine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine): Long

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines WHERE userId = :userId AND isTaken = 0")
    fun getPendingMedicines(userId: String): Flow<List<Medicine>>
} 