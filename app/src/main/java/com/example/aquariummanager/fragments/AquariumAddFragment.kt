package com.example.aquariummanager.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.aquariummanager.R
import com.example.aquariummanager.databinding.FragmentAquariumAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.io.File
import java.util.*

private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_PERMISSION = 100
private const val FILE_MAME = "photo.jpg"
class AquariumAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private lateinit var launcherPhoto : ActivityResultLauncher<Intent>
    private lateinit var launcherImage : ActivityResultLauncher<Intent>
    private lateinit var photoFile: File
    private lateinit var imageBit: Bitmap

    companion object {
        fun newInstance() = AquariumAddFragment()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAquariumAddBinding.inflate(inflater, container, false)

        if (viewModel.imageInitialized())
            binding.aquaPhoto.setImageBitmap(viewModel.imageBit)
        else
            binding.aquaPhoto.setImageResource(R.drawable.noimage)

        launcherPhoto = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            if (this::photoFile.isInitialized) {
                imageBit = BitmapFactory.decodeFile(photoFile.absolutePath)
                setImageBitmap(imageBit)
            }

        }
        launcherImage = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            val image = result.data?.data
            imageBit = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver,image!!))
            setImageBitmap(imageBit)
        }

        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setDateButton.setOnClickListener(){
            pickDate();
        }
        binding.saveButton.setOnClickListener {
            if(createAquarium()){
                viewModel.imageBit = BitmapFactory.decodeResource(resources, R.drawable.noimage)
                findNavController().popBackStack()
            }
        }

        binding.addPhotoButton.setOnClickListener {
            val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, loadPhoto());

            launcherPhoto.launch(photoIntent)
        }
        binding.addImageButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT)
            imageIntent.addCategory(Intent.CATEGORY_OPENABLE)
            imageIntent.type = "image/*"

            launcherImage.launch(imageIntent)
        }
    }

    private fun getPhotoFile(fileMame: String): File {
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileMame, ".jpg", storageDir);
    }

    private fun loadPhoto():Uri {
        photoFile = getPhotoFile(FILE_MAME)
        return FileProvider.getUriForFile(requireContext(), "com.example.aquariummanager.fileprovider", photoFile)
    }

    private fun setImageBitmap(imageBitmap:Bitmap) {
        viewModel.imageBit = imageBitmap
        binding.aquaPhoto.setImageBitmap(imageBitmap)
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

    private fun createAquarium():Boolean {

        if (binding.editTextName.text.toString() == "" &&
            binding.editTextDate.text.toString() == "Start date" &&
            binding.editTextVolume.text.toString() == ""){
            Toast.makeText(requireContext(), "please select name, date and volume", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.editTextName.text.toString() == "" &&
            binding.editTextDate.text.toString() == "Start date"){
            Toast.makeText(requireContext(), "please select name and date", Toast.LENGTH_LONG).show()
            return false
        }else if (binding.editTextName.text.toString() == "" &&
            binding.editTextVolume.text.toString() == ""){
            Toast.makeText(requireContext(), "please select name and volume", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.editTextDate.text.toString() == "Start date" &&
            binding.editTextVolume.text.toString() == ""){
            Toast.makeText(requireContext(), "please select date and volume", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.editTextName.text.toString() == ""){
            Toast.makeText(requireContext(), "please select name", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.editTextDate.text.toString() == "Start date"){
            Toast.makeText(requireContext(), "please select date", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.editTextVolume.text.toString() == ""){
            Toast.makeText(requireContext(), "please select volume", Toast.LENGTH_LONG).show()
            return false
        }

        val name:String = binding.editTextName.text.toString();
        val date:String = binding.editTextDate.text.toString()
        val volume:Double = binding.editTextVolume.text.toString().toDouble();
        val desc:String = binding.editTextDescription.text.toString()

        val param:Double = if (binding.editTextParam.text.toString() == ""){
            0.0
        } else{
            binding.editTextParam.text.toString().toDouble();
        }

        if (this::imageBit.isInitialized)
            viewModel.addAquarium(name, imageBit, date, volume, desc, param)
        else
            viewModel.addAquarium(name, BitmapFactory.decodeResource(resources, R.drawable.stock), date, volume, desc, param)
        return true
    }
}

