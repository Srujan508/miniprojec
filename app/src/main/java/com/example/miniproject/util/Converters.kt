package com.example.miniproject.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalTimeList(value: List<LocalTime>): String {
        return gson.toJson(value.map { it.toString() })
    }

    @TypeConverter
    fun toLocalTimeList(value: String): List<LocalTime> {
        val listType = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(value, listType)
        return stringList.map { LocalTime.parse(it) }
    }
} 