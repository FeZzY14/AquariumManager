package com.example.aquariummanager.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.data.AquariumDao
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.databinding.FragmentAquariumPropertiesBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel

class AquariumPropertiesFragment : Fragment() {

    private lateinit var binding: FragmentAquariumPropertiesBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumPropertiesFragmentArgs by navArgs()
    private lateinit var aquarium:AquariumItem
    private var position = 0;
    private lateinit var aquariumDao: AquariumDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquarium_properties, container, false)
        position = args.position
        aquarium = viewModel.getItem(position)
        binding.aquarium = aquarium
        val imageByte = aquarium.image

        binding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte!!.size))
        aquariumDao = Database.getInstance(requireContext()).aquariumDao()

        binding.buttonEquipment.setOnClickListener{
            val direction = AquariumPropertiesFragmentDirections.actionAquariumPropertiesFragmentToAquariumItemsFragment("equipment", position)
            it.findNavController().navigate(direction)
        }

        binding.buttonInhabitants.setOnClickListener{
            val direction = AquariumPropertiesFragmentDirections.actionAquariumPropertiesFragmentToAquariumItemsFragment("inhabitant", position)
            it.findNavController().navigate(direction)
        }

        binding.buttonTasks.setOnClickListener{
            val direction = AquariumPropertiesFragmentDirections.actionAquariumPropertiesFragmentToAquariumItemsFragment("task", position)
            it.findNavController().navigate(direction)
        }

        binding.buttonMeasurements.setOnClickListener{
            val direction = AquariumPropertiesFragmentDirections.actionAquariumPropertiesFragmentToAquariumItemsFragment("measurement", position)
            it.findNavController().navigate(direction)
        }

        binding.saveButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmDialogue(it)
        }

        return binding.root;
    }

    /**
     * Slúži na zobrazenie okna s informáciou, či chceme naozaj odstrániť akvárium
     *
     * @param view pohľad
     */
    private fun showDeleteConfirmDialogue(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            viewModel.removeAquarium(args.position, requireContext());
            view.findNavController().popBackStack()
        })
        builder.setNegativeButton("No", null)
        val alert = builder.create()
        alert.setTitle("aquarium delete")
        alert.setMessage("Do you really want to delete this aquarium ?")
        alert.show()
    }
}