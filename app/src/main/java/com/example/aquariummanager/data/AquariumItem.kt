package com.example.aquariummanager.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "aquariums")
data class AquariumItem(
    @PrimaryKey val index : Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") var image: ByteArray?,
    @ColumnInfo(name = "date")val startDate: String,
    @ColumnInfo(name = "volume")val volume: Double,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "parameter")var measureParam: Double,
) {
    @Ignore val equipment = ArrayList<Item>()
    @Ignore val inhabitants = ArrayList<Item>()
    @Ignore val measurements = ArrayList<Measurement>()
    @Ignore val tasks = ArrayList<Task>()



}