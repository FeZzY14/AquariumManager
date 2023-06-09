package com.example.aquariummanager.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.aquariummanager.data.*
import com.example.aquariummanager.databinding.FragmentDashboardBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private lateinit var aquariumDao: AquariumDao
    private lateinit var itemDao: ItemDao
    private lateinit var taskDao: TaskDao
    private lateinit var measurementDao: MeasurementDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container,false)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialogue()
            }
        })

        aquariumDao = Database.getInstance(requireContext()).aquariumDao()
        itemDao = Database.getInstance(requireContext()).itemDao()
        taskDao = Database.getInstance(requireContext()).taskDao()
        measurementDao = Database.getInstance(requireContext()).measurementDao()

        if (viewModel.list.isEmpty())
        {
            initFromDatabase()
        }

        val todayList = viewModel.getTasks(fun(d1: LocalDate, d2:LocalDate):Boolean{return d1 == d2})
        val upcomingList = viewModel.getTasks(fun(d1: LocalDate, d2:LocalDate):Boolean{return d1 > d2})
        val overList = viewModel.getTasks(fun(d1: LocalDate, d2:LocalDate):Boolean{return d1 < d2})

        viewModel.todayList = todayList
        viewModel.upcomingList = upcomingList
        viewModel.overList = overList

        binding.todayButton.setOnClickListener {
            val direction = DashboardFragmentDirections.actionDashboardFragmentToAquariumItemsFragment("todayTasks", 0)
            it.findNavController().navigate(direction)
        }

        binding.overButton.setOnClickListener {
            val direction = DashboardFragmentDirections.actionDashboardFragmentToAquariumItemsFragment("overTasks", 0)
            it.findNavController().navigate(direction)
        }

        binding.upButton.setOnClickListener {
            val direction = DashboardFragmentDirections.actionDashboardFragmentToAquariumItemsFragment("upTasks", 0)
            it.findNavController().navigate(direction)
        }

        binding.aquaCount = viewModel.list.count().toString()
        binding.todayCount = todayList.count()
        binding.overCount = overList.count()
        binding.upCount = upcomingList.count()

        return binding.root;
    }

    /**
     * Slúži na načítanie všetkých položiek a akvárií z databázy
     *
     */
    private fun initFromDatabase() {
        val aquariums = aquariumDao.getAll()

        for (i in aquariums.indices) {
            viewModel.list.add(aquariums[i])
        }

        val items = itemDao.getAll()

        for (i in items.indices) {
            val item = items[i]

            if (item.dateText == "install") {
                viewModel.list[item.index].equipment.add(item)
            } else {
                viewModel.list[item.index].inhabitants.add(item)
            }
        }

        val tasks = taskDao.getAll()

        for (i in tasks.indices)
        {
            viewModel.list[tasks[i].index].tasks.add(tasks[i])
        }

        val measurement = measurementDao.getAll()

        for (i in measurement.indices)
        {
            viewModel.list[measurement[i].index].measurements.add(measurement[i])
        }
    }

    /**
     * Slúži na zobrazenie okna s informáciou, či chceme naozaj ukončiť aplikáciu
     *
     * @param view pohľad
     */
    private fun showExitDialogue() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("do you want to exit?")
            .setCancelable(false)
            .setNegativeButton("no"){ _,_->
            }
            .setPositiveButton("yes") { _,_->
                requireActivity().finish()
            }
            .show()
    }


}