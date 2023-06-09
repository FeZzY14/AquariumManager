package com.example.aquariummanager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.adapters.AquariumItemAdapter
import com.example.aquariummanager.adapters.AquariumMeasurementAdapter
import com.example.aquariummanager.adapters.AquariumTaskAdapter
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.databinding.FragmentAquariumItemsBinding
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

        binding.itemType = args.itemType

        when (args.itemType) {
            "task" -> {
                binding.noItems = aquarium.tasks.isNotEmpty()
                binding.addButton.setOnClickListener {
                    val direction = AquariumItemsFragmentDirections.actionAquariumItemsFragmentToAquariumTaskAddFragment(args.position)
                    it.findNavController().navigate(direction)
                }
                val adapter = AquariumTaskAdapter(viewModel.list[args.position].tasks,false)
                binding.list.adapter = adapter
            }
            "inhabitant" -> {
                binding.noItems = aquarium.inhabitants.isNotEmpty()
                binding.addButton.setOnClickListener {
                    val direction = AquariumItemsFragmentDirections.actionAquariumItemsFragmentToAquariumItemAddFragment(args.itemType, args.position)
                    it.findNavController().navigate(direction)
                }
                val adapter = AquariumItemAdapter(viewModel.list[args.position].inhabitants)
                binding.list.adapter = adapter
            }
            "equipment" -> {
                binding.noItems = aquarium.equipment.isNotEmpty()
                binding.addButton.setOnClickListener {
                    val direction = AquariumItemsFragmentDirections.actionAquariumItemsFragmentToAquariumItemAddFragment(args.itemType, args.position)
                    it.findNavController().navigate(direction)
                }
                val adapter = AquariumItemAdapter(viewModel.list[args.position].equipment)
                binding.list.adapter = adapter
            }
            "measurement" -> {
                binding.noItems = aquarium.measurements.isNotEmpty()
                binding.addButton.setOnClickListener {
                    val direction = AquariumItemsFragmentDirections.actionAquariumItemsFragmentToAquariumMeasurementAddFragment(args.position)
                    it.findNavController().navigate(direction)
                }
                val adapter = AquariumMeasurementAdapter(viewModel.list[args.position].measurements)
                binding.list.adapter = adapter
            }
            "todayTasks" -> {
                binding.noItems = true
                binding.addButtonGone = true
                val adapter = AquariumTaskAdapter(viewModel.todayList,true)
                binding.list.adapter = adapter
            }
            "overTasks" -> {
                binding.noItems = true
                binding.addButtonGone = true
                val adapter = AquariumTaskAdapter(viewModel.overList,true)
                binding.list.adapter = adapter
            }
            "upTasks" -> {
                binding.noItems = true
                binding.addButtonGone = true
                val adapter = AquariumTaskAdapter(viewModel.upcomingList,true)
                binding.list.adapter = adapter
            }

        }

        viewModel.imageByteArray = null

        return binding.root
    }
}