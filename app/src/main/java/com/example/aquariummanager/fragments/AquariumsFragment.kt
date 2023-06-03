package com.example.aquariummanager.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aquariummanager.R
import com.example.aquariummanager.adapters.AquariumsAdapter
import com.example.aquariummanager.databinding.FragmentAquariumsBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

class AquariumsFragment : Fragment() {

    private lateinit var binding: FragmentAquariumsBinding
    private val viewModel: AquariumsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquariums ,container, false)

        val adapter = AquariumsAdapter(viewModel.list)
        binding.aquariumList.adapter = adapter

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialogue()
            }
        })

        binding.noAquariums = viewModel.list.isNotEmpty()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addButton.setOnClickListener {

            findNavController().navigate(R.id.action_aquariumsFragment_to_aquariumAddFragment);
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