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
import com.example.aquariummanager.data.*
import com.example.aquariummanager.databinding.FragmentAquariumItemAddBinding
import com.example.aquariummanager.databinding.FragmentAquariumMeasurementAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AquariumMeasurementAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumMeasurementAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumMeasurementAddFragmentArgs by navArgs()
    private lateinit var aquarium: AquariumItem
    private lateinit var measurementDao: MeasurementDao

    private lateinit var date: LocalDate
    private lateinit var time: LocalTime

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquarium_measurement_add, container, false)
        binding.timePicker.setIs24HourView(true)

        binding.timePicker.setOnTimeChangedListener { timePicker, i, i2 ->
            val timeVal = LocalTime.of(timePicker.hour, timePicker.minute)
            time = timeVal
            viewModel.time = timeVal
        }

        binding.setDateButton.setOnClickListener {
            pickDate()
        }

        binding.saveButton.setOnClickListener {
            if(createMeasurement()){
                viewModel.date = null
                viewModel.time = null
                viewModel.dateTask = null
                findNavController().popBackStack()
            }
        }

        if (viewModel.date != null)
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

        measurementDao = Database.getInstance(requireContext()).measurementDao()

        return binding.root
    }

    /**
     * Vytvorenie nového merania, pokiaľ sme nezadali všetky požadované parametre, zobrazí sa Toast s informáciou
     * a meranie sa nevytvorí.
     *
     * @return true pokiaľ sa meranie vytvorí inak false
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMeasurement(): Boolean {
        if (!showToast())
        {
            return false
        }

        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val value = binding.editTextMeasurement.text.toString().toDouble()
        val units = binding.editTextUnits.text.toString()

        val measurement = Measurement(0,args.position, name, description, value, units,
            date.dayOfMonth, date.monthValue, date.year, time.hour, time.minute)
        viewModel.list[args.position].measurements.add(measurement)
        measurementDao.insertAll(measurement)
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
            !this::time.isInitialized ||
            binding.editTextMeasurement.text.toString() == "" ||
            binding.editTextUnits.text.toString() == "")
        {
            Toast.makeText(requireContext(), "please select name, measurement, units, date and time", Toast.LENGTH_LONG).show()
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