package com.example.aquariummanager.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
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
import com.example.aquariummanager.data.AquariumDao
import com.example.aquariummanager.data.AquariumItem
import com.example.aquariummanager.data.Database
import com.example.aquariummanager.databinding.FragmentAquariumAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


private const val FILE_MAME = "photo.jpg"
class AquariumAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private lateinit var launcherPhoto : ActivityResultLauncher<Intent>
    private lateinit var launcherImage : ActivityResultLauncher<Intent>
    private lateinit var photoFile: File
    private lateinit var imageBit: Bitmap
    private lateinit var aquariumDao: AquariumDao

    companion object {
        fun newInstance() = AquariumAddFragment()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAquariumAddBinding.inflate(inflater, container, false)


        if (viewModel.imageByteArray != null) {
            val image = BitmapFactory.decodeByteArray(viewModel.imageByteArray, 0, viewModel.imageByteArray!!.size);
            binding.aquaPhoto.setImageBitmap(image)
        }
        else
            binding.aquaPhoto.setImageResource(R.drawable.noimage)

        if (viewModel.date != null)
        {
            binding.editTextDate.text = viewModel.date
        }

        launcherPhoto = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) {
            if (this::photoFile.isInitialized) {
                imageBit = BitmapFactory.decodeFile(photoFile.absolutePath)
                imageBit = Bitmap.createScaledBitmap(imageBit, imageBit.width/10,
                    imageBit.height/10, false)
                setImageBitmap(imageBit)
            }

        }
        launcherImage = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            val image = result.data?.data
            imageBit = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver,image!!))
            imageBit = Bitmap.createScaledBitmap(imageBit, imageBit.width/10,
                imageBit.height/10, false)
            setImageBitmap(imageBit)
        }

        aquariumDao = Database.getInstance(requireContext()).aquariumDao()

        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setDateButton.setOnClickListener(){
            pickDate();
        }
        binding.saveButton.setOnClickListener {
            if(createAquarium()){
                val imageBit = BitmapFactory.decodeResource(resources, R.drawable.noimage)
                viewModel.imageByteArray = getBitmapAsByteArray(imageBit)
                viewModel.date = null
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

    /**
     * Slúži na vytvorenie nového dočasného súboru pre fotku.
     *
     * @param fileMame názov súboru
     * @return nový dočasný súbor
     */
    private fun getPhotoFile(fileMame: String): File {
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileMame, ".jpg", storageDir);
    }

    /**
     * Služi na získanie Uri zo súboru s fotkou.
     *
     * @return Uri vytvorené z fotky.
     */
    private fun loadPhoto():Uri {
        photoFile = getPhotoFile(FILE_MAME)
        return FileProvider.getUriForFile(requireContext(), "com.example.aquariummanager.fileprovider", photoFile)
    }

    /**
     * Slúži na uloženie Bitmap fotky do view modelu a nastavenie obrázku v layoute.
     *
     * @param imageBitmap bitmap z fotky
     */
    private fun setImageBitmap(imageBitmap:Bitmap) {
        binding.aquaPhoto.setImageBitmap(imageBitmap)
        viewModel.imageByteArray = getBitmapAsByteArray(imageBitmap)
    }

    /**
     * Vytvorenie pola bytov z Bitmap.
     *
     * @param imageBitmap Bitmap z fotky
     * @return pole bytov reprezentujúce fotku
     */
    private fun getBitmapAsByteArray(imageBitmap: Bitmap): ByteArray
    {
        val outputStream = ByteArrayOutputStream()
        imageBitmap.compress(CompressFormat.PNG, 0, outputStream)
        val byt = outputStream.toByteArray()
        outputStream.close()
        return byt
    }

    /**
     * Slúži na vybratie dátumu a uloženie vybraného dátumu do view modelu
     *
     */
    private fun pickDate() {
        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        val day = calendar.get(Calendar.DATE);
        val dataPickerDialog = DatePickerDialog(
            requireContext(), { view, year, monthofYear, dayOfMonth ->
                val dateText = dayOfMonth.toString() + "." + (monthofYear + 1) + "." + year;
                viewModel.date = dateText;
                binding.editTextDate.setText(dateText)
            }, year, month, day
        )
        dataPickerDialog.show()
    }

    /**
     * Vytvorenie nového akvária, pokiaľ sme nezadali všetky požadované parametre, zobrazí sa Toast s informáciou
     * a akvárium sa nevytvorí.
     *
     * @return true pokiaľ sa akvárium vytvorilo inak false
     */
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

        if (viewModel.imageByteArray != null) {
            val aquarium = AquariumItem(viewModel.list.size, name, viewModel.imageByteArray, date, volume, desc, param)
            viewModel.list.add(aquarium)
            aquariumDao.insertAll(aquarium)
        }
        else {
            var imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.stock)
            val aquarium = AquariumItem(viewModel.list.size, name, getBitmapAsByteArray(imageBitmap), date, volume, desc, param)
            viewModel.list.add(aquarium)
            aquariumDao.insertAll(aquarium)
        }
            return true
    }
}

