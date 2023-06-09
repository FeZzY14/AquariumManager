package com.example.aquariummanager.fragments

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.data.Task
import com.example.aquariummanager.data.TaskDao
import com.example.aquariummanager.databinding.FragmentAquariumTaskAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AquariumTaskAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumTaskAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumTaskAddFragmentArgs by navArgs()
    private lateinit var taskDao: TaskDao

    private lateinit var date: LocalDate
    private lateinit var time: LocalTime

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquarium_task_add, container, false)
        binding.timePicker.setIs24HourView(true)
        taskDao = Database.getInstance(requireContext()).taskDao()

        binding.timePicker.setOnTimeChangedListener { timePicker, i, i2 ->
            val timeVal = LocalTime.of(timePicker.hour, timePicker.minute)
            time = timeVal
            viewModel.time = timeVal
        }

        binding.setDateButton.setOnClickListener {
            pickDate()
        }

        binding.saveButton.setOnClickListener {
            if(createTask()){
                findNavController().popBackStack()
            }
        }

        if (viewModel.date != null && viewModel.dateTask != null)
        {
            binding.editTextDate.text = viewModel.date
            date = viewModel.dateTask!!
        }

        if (viewModel.time != null)
        {
            time = viewModel.time!!
            binding.timePicker.hour = time.hour
            binding.timePicker.minute = time.minute
        }

        return binding.root
    }

    /**
     * Vytvorenie novej úlohy, pokiaľ sme nezadali všetky požadované parametre, zobrazí sa Toast s informáciou
     * a úloha sa nevytvorí.
     *
     * @return true pokiaľ sa úloha vytvorila inak false
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTask(): Boolean {
        if (!showToast())
        {
            return false
        }

        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()

        val task = Task(0, args.position, name, description, date.dayOfMonth, date.monthValue, date.year, time.hour, time.minute)
        taskDao.insertAll(task)
        viewModel.list[args.position].tasks.add(task)
        return true
    }

    /**
     * pokiaľ sme nezadali všetky potrebné parametre, zobrazení Toast s informáciou
     *
     * @return true pokiaľ sa Toast nezobrazil inak false
     */
    private fun showToast():Boolean {
        if (binding.editTextName.text.toString() == "" ||
            !this::date.isInitialized ||
                !this::time.isInitialized)
        {
            Toast.makeText(requireContext(), "please select name, date and time", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    /**
     * Slúži na vybratie dátumu a uloženie vybraného dátumu do view modelu
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun pickDate() {
        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        val day = calendar.get(Calendar.DATE);
        val dataPickerDialog = DatePickerDialog(
            requireContext(), { view, year, monthOfYear, dayOfMonth ->
                val dateText = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year;
                viewModel.date = dateText;
                binding.editTextDate.setText(dateText)
                viewModel.dateTask = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                date = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
            }, year, month, day
        )
        dataPickerDialog.show()
    }

}