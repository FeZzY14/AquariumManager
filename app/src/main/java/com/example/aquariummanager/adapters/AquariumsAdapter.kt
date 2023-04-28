package com.example.aquariummanager.adapters

import android.content.Context
import android.graphics.Path.Direction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.aquariummanager.R
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.databinding.FragmentCalculatorBinding
import com.example.aquariummanager.databinding.ListItemAquariumBinding
import com.example.aquariummanager.fragments.AquariumsFragmentDirections

class AquariumsAdapter(val items:ArrayList<AquariumItem>):
    RecyclerView.Adapter<AquariumsAdapter.ViewHolder>() {



    class ViewHolder(private val binding: ListItemAquariumBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: AquariumItem, position: Int){
                binding.apply {
                    aquarium = item
                    executePendingBindings()
                }
                binding.aquaImage.setImageBitmap(item.image)
                binding.setClickListener {
                    val direction = AquariumsFragmentDirections.actionAquariumsFragmentToAquariumPropertiesFragment(position);
                    it.findNavController().navigate(direction)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_aquarium,
                parent, false))
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aquarium = items[position];
        holder.bind(aquarium, position);
    }

}