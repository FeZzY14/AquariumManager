package com.example.aquariummanager.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aquariummanager.R
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.data.Item
import com.example.aquariummanager.data.Task
import com.example.aquariummanager.databinding.ListItemAquariumItemBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * adaptér pre úlohy, používaný pre zobrazovanie úloh v RecyclerView
 *
 * @property tasks zoznam všetkých úloh
 * @property deleteButtonIsGone boolean hodnota či sa ma zobraziť tlačidlo na vymazanie alebo nie
 */
class AquariumTaskAdapter(val tasks:ArrayList<Task>, val deleteButtonIsGone: Boolean):
    RecyclerView.Adapter<AquariumTaskAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemAquariumItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Task, position: Int){
            binding.apply {
                varName = item.name
                varDate = LocalDate.of(item.year, item.month, item.day).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
                dateText = "task"
                varTime = LocalTime.of(item.hours, item.minutes).toString()
                varDescription = item.description
                isGone = false
                deleteButtonGone = deleteButtonIsGone
                executePendingBindings()
                binding.deleteButton.setOnClickListener {
                    remove(position, it.context)
                }
            }

        }
    }

    /**
     * Vymazanie úlohy zo zoznamu úloh a z databázy
     *
     * @param position index úlohy ktorá sa má vymazať
     * @param context context pre databázu
     */
    fun remove(position: Int, context: Context)
    {
        val itemDao = Database.getInstance(context).taskDao()
        itemDao.delete(tasks[position])
        tasks.removeAt(position)
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
        return tasks.size;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position];
        holder.bind(task, position);
    }



}