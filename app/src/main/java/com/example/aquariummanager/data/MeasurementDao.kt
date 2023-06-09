package com.example.aquariummanager.data

import androidx.room.*

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurements")
    fun getAll(): List<Measurement>

    @Insert
    fun insertAll(vararg measurement: Measurement)

    @Update
    fun update(measurement: Measurement)

    @Delete
    fun delete(measurement: Measurement)
}