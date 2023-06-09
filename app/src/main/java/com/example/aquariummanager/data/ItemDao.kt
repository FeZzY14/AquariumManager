package com.example.aquariummanager.data

import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<Item>

    @Insert
    fun insertAll(vararg item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}