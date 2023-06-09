package com.example.aquariummanager.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.StringBufferInputStream

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @ColumnInfo(name = "index") val index: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "image") var image: ByteArray?,
    @ColumnInfo(name = "dateText") val dateText: String,
    ) {}