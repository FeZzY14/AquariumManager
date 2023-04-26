package com.example.aquariummanager.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.aquariummanager.R
import com.example.aquariummanager.databinding.FragmentAquariumAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.util.*

class AquariumAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    //private val aquariumsList = AquariumsList();

    companion object {
        fun newInstance() = AquariumAddFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAquariumAddBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setDateButton.setOnClickListener(){
            pickDate();
        }
        binding.saveButton.setOnClickListener {
            createAquarium();
            findNavController().popBackStack()
        }
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        val day = calendar.get(Calendar.DATE);
        val dataPickerDialog = DatePickerDialog(
            requireContext(), { view, year, monthofYear, dayOfMonth ->
                binding.editTextDate.setText(dayOfMonth.toString() + "." + (monthofYear + 1) + "." + year)
            }, year, month, day
        )
        dataPickerDialog.show()
    }

    private fun createAquarium() {
        val name = binding.editTextName.text.toString();
        val date = binding.editTextDate.text.toString();
        val volume = binding.editTextVolume.text.toString().toDouble();
        val param = binding.editTextParam.text.toString().toDouble();
        val desc = binding.editTextDescription.text.toString()

        viewModel.addAquarium(name, R.drawable.diagram4c, date, volume, desc, param)
    }
}