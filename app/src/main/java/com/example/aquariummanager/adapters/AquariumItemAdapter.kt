package com.example.aquariummanager.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aquariummanager.R
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.data.Item
import com.example.aquariummanager.data.ItemDao
import com.example.aquariummanager.databinding.ListItemAquariumItemBinding
import java.nio.file.Files.delete

/**
 * adaptér pre položky, používaný pre zobrazovanie položiek v RecyclerView
 *
 * @property items zoznam všetkých položiek
 */
class AquariumItemAdapter(val items:ArrayList<Item>):
    RecyclerView.Adapter<AquariumItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemAquariumItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, position: Int){
            binding.apply {
                varName = item.name
                varDate = item.date
                dateText = item.dateText
                varDescription = item.description
                executePendingBindings()
            }
            val imageByte = item.image
            binding.image.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte!!.size))
            binding.deleteButton.setOnClickListener {
                remove(position,it.context)
            }
        }
    }

    /**
     * Vymazanie prvku zo zoznamu prvkov a z databázy
     *
     * @param position index prvku ktory sa má vymazať
     * @param context context pre databázu
     */
    fun remove(position: Int, context: Context)
    {
        val itemDao = Database.getInstance(context).itemDao()
        itemDao.delete(items[position])
        items.removeAt(position)
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
        return items.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position];
        holder.bind(item, position);
    }

}