package com.example.aquariummanager.viewModels

import androidx.lifecycle.ViewModel
import com.example.aquariummanager.data.AquariumItem

class AquariumsViewModel : ViewModel() {
    val list = ArrayList<AquariumItem>();


    fun addAquarium(name:String, image:Int, date: String,
                    volume:Double, description:String, param:Double) {

        list.add(AquariumItem(name, image,date,volume,description, param))
    }

    fun getItem(index:Int): AquariumItem {
        return list.get(index);
    }
}