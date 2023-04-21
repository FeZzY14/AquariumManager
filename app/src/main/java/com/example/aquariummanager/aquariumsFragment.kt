package com.example.aquariummanager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aquariummanager.databinding.FragmentAquariumsBinding
import com.example.aquariummanager.databinding.FragmentDashboardBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel

class aquariumsFragment : Fragment() {

    companion object {
        fun newInstance() = aquariumsFragment()
    }

    lateinit var binding: FragmentAquariumsBinding
    private lateinit var viewModel: AquariumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAquariumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AquariumsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}