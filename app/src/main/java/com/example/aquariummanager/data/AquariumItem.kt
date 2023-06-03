package com.example.aquariummanager.data

import android.graphics.Bitmap

data class AquariumItem(
    val name: String,
    var image: Bitmap,
    val startDate: String,
    val volume: Double,
    val description: String,
    var measureParam: Double,
) {
    val equipment = ArrayList<String>()
    val inhabitants = ArrayList<String>()
    val measurements = ArrayList<Int>()
    val tasks = ArrayList<String>()
    fun addEquipment(newEquipment: String) {
        equipment.add(newEquipment)
    }

    fun addInhabitant(newInhabitant: String) {
        inhabitants.add(newInhabitant)
    }

}