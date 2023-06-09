package com.example.aquariummanager.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @ColumnInfo(name = "index") val index : Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "value") val value: Double,
    @ColumnInfo(name = "units") val units: String,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "hours") val hours: Int,
    @ColumnInfo(name = "minutes") val minutes: Int,
) {}