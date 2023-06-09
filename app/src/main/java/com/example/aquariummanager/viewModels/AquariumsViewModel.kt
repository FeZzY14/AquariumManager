package com.example.aquariummanager.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.data.Item
import com.example.aquariummanager.data.Task
import java.time.LocalDate
import java.time.LocalTime

class AquariumsViewModel : ViewModel() {
    val list = ArrayList<AquariumItem>()
    var imageByteArray:ByteArray? = null
    var date:String? = null;
    var dateTask:LocalDate? = null;
    var time: LocalTime? = null;

    lateinit var todayList: ArrayList<Task>
    lateinit var upcomingList: ArrayList<Task>
    lateinit var overList: ArrayList<Task>

    /**
     * Slúži na vymazanie akvária z databázy a taktiež na vymazanie všetkých položiek tohoto akvária z databázy
     *
     * @param position index akvária, ktoré sa má vymazať
     * @param context kontext pre databázu
     */
    fun removeAquarium(position: Int, context: Context)
    {
        val aquariumDao = Database.getInstance(context).aquariumDao()
        val itemDao = Database.getInstance(context).itemDao()
        val taskDao = Database.getInstance(context).taskDao()
        val measurementDao = Database.getInstance(context).measurementDao()

        val aquarium = list[position]

        for (i in aquarium.inhabitants.indices)
        {
            itemDao.delete(aquarium.inhabitants[i])
        }

        for (i in aquarium.equipment.indices)
        {
            itemDao.delete(aquarium.equipment[i])
        }

        for (i in aquarium.tasks.indices)
        {
            taskDao.delete(aquarium.tasks[i])
        }

        for (i in aquarium.measurements.indices)
        {
            measurementDao.delete(aquarium.measurements[i])
        }

        aquariumDao.delete(aquarium)
        list.removeAt(position)
    }

    /**
     * Slúži na pridanie nového zariadenia do view modelu
     *
     * @param equipment zariadenia, ktoré sa má pridať
     * @param position index akvária, ku ktorému zariadenie patrí
     */
    fun addEquipment(equipment: Item, position: Int) {
        list[position].equipment.add(equipment)
    }

    /**
     * Slúži na pridanie nového živočícha do view modelu
     *
     * @param inh živočích, ktorý sa má pridať
     * @param position index akvária, ku ktorému živočích patrí
     */
    fun addInhabitant(inh: Item, position: Int) {
        list[position].inhabitants.add(inh)
    }

    /**
     * Vráti akvárium na pozií podľa parametra
     *
     * @param index pozícia akvária v zozname
     * @return akvárium na pozícií
     */
    fun getItem(index:Int): AquariumItem {
        return list[index];
    }

    /**
     * Slúži na získanie všetkých úloh podľa komparátora
     *
     * @param func funkcia ktorá ako parametre preberá dva dátumu a vráti ich porovnanie
     * @return list úloh podľa komparátoro
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTasks(func: (LocalDate, LocalDate) -> Boolean): ArrayList<Task> {
        val todayList = ArrayList<Task>()
        val today = LocalDate.now()

        for(i in list.indices) {
            for (j in list[i].tasks.indices)
            {
                if (func(LocalDate.of(list[i].tasks[j].year, list[i].tasks[j].month, list[i].tasks[j].day), today))
                {
                    todayList.add(list[i].tasks[j])
                }
            }
        }
        return todayList
    }
}