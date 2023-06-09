package com.example.aquariummanager.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aquariummanager.R
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.data.Measurement
import com.example.aquariummanager.databinding.ListItemAquariumItemBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * adaptér pre merania, používaný pre zobrazovanie meraní v RecyclerView
 *
 * @property measurements zoznam všetkých meraní
 */
class AquariumMeasurementAdapter(val measurements: ArrayList<Measurement>):
    RecyclerView.Adapter<AquariumMeasurementAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemAquariumItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Measurement, position: Int){
            binding.apply {
                varName = item.name
                varDate = LocalDate.of(item.year, item.month, item.day).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()+
                        " " + LocalTime.of(item.hours, item.minutes).toString()
                dateText = "measurement"
                varTime = item.value.toString() + "\n" + item.units
                varDescription = item.description
                isGone = false
                executePendingBindings()
            }
            binding.deleteButton.setOnClickListener {
                remove(position, it.context)
            }
        }
    }

    /**
     * Vymazanie merania zo zoznamu meraní a z databázy
     *
     * @param position index merania ktory sa má vymazať
     * @param context context pre databázu
     */
    fun remove(position: Int, context: Context)
    {
        val measurementDao = Database.getInstance(context).measurementDao()
        measurementDao.delete(measurements[position])
        measurements.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_aquarium_item,
                parent, false))
    }

    override fun getItemCount(): Int {
        return measurements.size;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = measurements[position];
        holder.bind(task, position);
    }

}