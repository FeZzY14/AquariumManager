package com.example.aquariummanager

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.aquariummanager.databinding.ActivityMainBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> binding.navHost.findNavController().navigate(R.id.action_global_dashboardFragment)
                R.id.aquariums -> binding.navHost.findNavController().navigate(R.id.action_global_aquariumsFragment)
                R.id.calculator -> binding.navHost.findNavController().navigate(R.id.action_global_calculatorFragment)
            }
            true
        }
    }
}