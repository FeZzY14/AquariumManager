package com.example.aquariummanager.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.databinding.FragmentAquariumItemsBinding
import com.example.aquariummanager.databinding.FragmentAquariumsBinding
import com.example.aquariummanager.viewModels.AquariumItemsViewModel
import com.example.aquariummanager.viewModels.AquariumsViewModel

class AquariumItemsFragment : Fragment() {

    private lateinit var binding: FragmentAquariumItemsBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumItemsFragmentArgs by navArgs()
    private lateinit var aquarium: AquariumItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquarium_items ,container, false)
        aquarium = viewModel.getItem(args.position)
        //val adapter

        when (args.itemType) {
            "task" -> {
                binding.itemType = "task"
                binding.noItems = aquarium.tasks.isNotEmpty()
            }
            "inhabitant" -> {
                binding.itemType = "inhabitant"
                binding.noItems = aquarium.inhabitants.isNotEmpty()
            }
            "equipment" -> {
                binding.itemType = "equipment"
                binding.noItems = aquarium.equipment.isNotEmpty()
            }
            "measurement" -> {
                binding.itemType = "measurement"
                binding.noItems = aquarium.measurements.isNotEmpty()
            }

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}