package com.example.aquariummanager.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.databinding.FragmentAqariumPropertiesBinding
import com.example.aquariummanager.viewModels.AqariumPropertiesViewModel
import com.example.aquariummanager.viewModels.AquariumsViewModel

class AquariumPropertiesFragment : Fragment() {

    companion object {
        fun newInstance() = AquariumPropertiesFragment()
    }

    private lateinit var binding: FragmentAqariumPropertiesBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumPropertiesFragmentArgs by navArgs()
    private lateinit var aquarium:AquariumItem
    private var position = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aqarium_properties, container, false)
        position = args.position
        aquarium = viewModel.getItem(position)
        binding.aquarium = aquarium
        binding.imageView.setImageBitmap(aquarium.image)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}