package com.example.aquariummanager.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aquariummanager.data.AquariumItem

class AquariumsViewModel : ViewModel() {
    val list = ArrayList<AquariumItem>();
    lateinit var imageBit:Bitmap
    lateinit var currImage:LiveData<Bitmap>

    fun addAquarium(name:String, image: Bitmap, date: String,
                    volume:Double, description:String, param:Double) {

        list.add(AquariumItem(name, image,date,volume,description, param))

    }

    fun getItem(index:Int): AquariumItem {
        return list[index];
    }

    fun imageInitialized():Boolean{
        return this::imageBit.isInitialized;
    }
}