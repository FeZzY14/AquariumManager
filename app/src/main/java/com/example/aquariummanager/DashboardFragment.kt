package com.example.aquariummanager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.aquariummanager.databinding.FragmentDashboardBinding
import com.example.aquariummanager.viewModels.DashboardViewModel
import java.time.Clock.system

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container,false)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }




}