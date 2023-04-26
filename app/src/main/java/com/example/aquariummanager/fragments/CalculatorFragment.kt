package com.example.aquariummanager.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.aquariummanager.R
import com.example.aquariummanager.databinding.FragmentCalculatorBinding
import com.example.aquariummanager.viewModels.CalculatorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CalculatorFragment : Fragment(){

    lateinit var binding: FragmentCalculatorBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator,container, false)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialogue()
            }
        })
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calcViewModel = viewModel;
        binding.lifecycleOwner = viewLifecycleOwner;

        binding.lengthRadioGroup.setOnCheckedChangeListener {_, _ ->
            if (binding.mmRadioButton.isChecked) {
                viewModel.changeLengthUnits("mm")
            } else if (binding.cmRadioButton.isChecked) {
                viewModel.changeLengthUnits("cm")
            } else if (binding.inRadioButton.isChecked) {
                viewModel.changeLengthUnits("in")
            }
        }
        binding.volumeRadioGroup.setOnCheckedChangeListener {_, _ ->
            if (binding.litersButton.isChecked) {
                viewModel.changeVolumeUnits("liters")
            } else if (binding.gallonsButton.isChecked) {
                viewModel.changeVolumeUnits("gallons")
            }
        }

       //todo text watcher on input
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var length = 0.0
                var width = 0.0
                var height = 0.0
                var topInd = 0.0
                var subsHeig = 0.0
                var thick = 0.0

                if (binding.lengthTextInput.text!!.toString().isNotEmpty()){
                    length = binding.lengthTextInput.text!!.toString().toDouble();
                }
                if (binding.widthTextInput.text!!.toString().isNotEmpty()){
                    width = binding.widthTextInput.text!!.toString().toDouble();
                }
                if (binding.heightTextInput.text!!.toString().isNotEmpty()){
                    height = binding.heightTextInput.text!!.toString().toDouble();
                }
                if (binding.topIndTextInput.text!!.toString().isNotEmpty()){
                    topInd = binding.topIndTextInput.text!!.toString().toDouble();
                }
                if (binding.subsHeigTextInput.text!!.toString().isNotEmpty()){
                    subsHeig = binding.subsHeigTextInput.text!!.toString().toDouble();
                }
                if (binding.thicTextInput.text!!.toString().isNotEmpty()){
                    thick = binding.thicTextInput.text!!.toString().toDouble();
                }
                if (binding.cmRadioButton.isChecked){
                    viewModel.calculateVolumes(length, width, height, subsHeig, topInd, thick, "cm");
                } else if (binding.mmRadioButton.isChecked){
                    viewModel.calculateVolumes(length, width, height, subsHeig, topInd, thick, "mm");
                } else {
                    viewModel.calculateVolumes(length, width, height, subsHeig, topInd, thick, "in");
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.lengthTextInput.addTextChangedListener(textWatcher);
        binding.widthTextInput.addTextChangedListener(textWatcher);
        binding.heightTextInput.addTextChangedListener(textWatcher);
        binding.topIndTextInput.addTextChangedListener(textWatcher);
        binding.subsHeigTextInput.addTextChangedListener(textWatcher);
        binding.thicTextInput.addTextChangedListener(textWatcher);


        binding.clearButton.setOnClickListener {
            binding.lengthTextInput.setText("");
            binding.heightTextInput.setText("");
            binding.widthTextInput.setText("");
            binding.topIndTextInput.setText("");
            binding.subsHeigTextInput.setText("");
            binding.thicTextInput.setText("");
            viewModel.clear()
        }
    }

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