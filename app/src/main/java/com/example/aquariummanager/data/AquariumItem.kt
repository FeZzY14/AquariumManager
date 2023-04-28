package com.example.aquariummanager.data

import android.graphics.Bitmap
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

data class AquariumItem(
    val name: String,
    var image: Bitmap,
    val startDate: String,
    val volume: Double,
    val description: String,
    var measureParam: Double,
    ) {


}