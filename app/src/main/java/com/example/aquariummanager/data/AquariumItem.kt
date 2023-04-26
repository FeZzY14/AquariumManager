package com.example.aquariummanager.data

import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

data class AquariumItem(
    val name: String,
    var image: Int,
    val startDate: String,
    val volume: Double,
    val description: String,
    var measureParam: Double,
    ) {


}