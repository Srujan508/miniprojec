package com.example.miniproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val times: List<LocalTime>,
    val userId: String,
    val notes: String? = null,
    val isTaken: Boolean = false,
    val lastTaken: Long? = null
) 