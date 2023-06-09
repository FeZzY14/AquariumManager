package com.example.aquariummanager.data

import androidx.room.*

@Dao
interface AquariumDao {
    @Query("SELECT * FROM aquariums")
    fun getAll(): List<AquariumItem>

    @Insert
    fun insertAll(vararg aquarium: AquariumItem)

    @Update
    fun update(aquarium: AquariumItem)

    @Delete
    fun delete(aquarium: AquariumItem)
}