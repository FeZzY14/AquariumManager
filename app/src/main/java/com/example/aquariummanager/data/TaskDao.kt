package com.example.aquariummanager.data

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>

    @Insert
    fun insertAll(vararg task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}