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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aquariummanager.R
import com.example.aquariummanager.data.*
import com.example.aquariummanager.databinding.FragmentAquariumItemAddBinding
import com.example.aquariummanager.viewModels.AquariumsViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

private const val FILE_MAME = "photo.jpg"

class AquariumItemAddFragment : Fragment() {

    private lateinit var binding: FragmentAquariumItemAddBinding
    private val viewModel: AquariumsViewModel by activityViewModels()
    private val args: AquariumItemAddFragmentArgs by navArgs()
    private lateinit var aquarium: AquariumItem

    private lateinit var launcherPhoto : ActivityResultLauncher<Intent>
    private lateinit var launcherImage : ActivityResultLauncher<Intent>
    private lateinit var photoFile: File
    private lateinit var imageBit: Bitmap
    private lateinit var itemDao: ItemDao

    companion object {
        fun newInstance() = AquariumItemAddFragment()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aquarium_item_add, container, false)

        aquarium = viewModel.getItem(args.position);
        itemDao = Database.getInstance(requireContext()).itemDao()

        launcherPhoto = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            if (this::photoFile.isInitialized) {
                imageBit = BitmapFactory.decodeFile(photoFile.absolutePath)
                imageBit = Bitmap.createScaledBitmap(imageBit, imageBit.width/10, imageBit.height/10, false)
                setImageBitmap(imageBit)
            }

        }
        launcherImage = registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            val image = result.data?.data
            imageBit = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver,image!!))
            imageBit = Bitmap.createScaledBitmap(imageBit, imageBit.width/10, imageBit.height/10, false)
            setImageBitmap(imageBit)
        }

        if (viewModel.imageByteArray != null) {
            val image = BitmapFactory.decodeByteArray(viewModel.imageByteArray, 0, viewModel.imageByteArray!!.size);
            binding.aquaPhoto.setImageBitmap(image)
        }
        else
            binding.aquaPhoto.setImageResource(R.drawable.noimage)

        when(args.itemType)
        {
            "equipment" -> {
                binding.dateText = "install"
                binding.saveButton.setOnClickListener {
                }
            }
            "inhabitant" -> {
                binding.dateText = "adoption"
            }
        }

        if (viewModel.date != null)
        {
            binding.editTextDate.text = viewModel.date
        }
        else
        {
            binding.editTextDate.text = getString(R.string.item_date, binding.dateText)
        }

        binding.saveButton.setOnClickListener {
            if(createItem()){
                viewModel.date = null
                viewModel.time = null
                viewModel.dateTask = null
                findNavController().popBackStack()
            }
        }
        binding.setDateButton.setOnClickListener {
            pickDate()
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

        return binding.root
    }

    /**
     * Slúži na vytvorenie nového dočasného súboru pre fotku.
     *
     * @param fileMame názov súboru
     * @return nový dočasný súbor
     */
    private fun getPhotoFile(fileMame: String): File {
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileMame, ".jpeg", storageDir);
    }

    /**
     * Služi na získanie Uri zo súboru s fotkou.
     *
     * @return Uri vytvorené z fotky.
     */
    private fun loadPhoto(): Uri {
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
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
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
     * Vytvorenie novej položky, pokiaľ sme nezadali všetky požadované parametre, zobrazí sa Toast s informáciou
     * a položka sa nevytvorí.
     *
     * @return true pokiaľ sa položka vytvorila inak false
     */
    private fun createItem():Boolean {
        if (!showToast())
        {
            return false
        }

        val name = binding.editTextName.text.toString()
        val date = binding.editTextDate.text.toString()
        val description = binding.editTextDescription.text.toString()

        if (args.itemType == "equipment")
        {
            if (this::imageBit.isInitialized) {
                val item = Item(0 ,args.position,name,description,date, viewModel.imageByteArray,"install")
                viewModel.addEquipment(item ,args.position);
                itemDao.insertAll(item)
                }
            else {
                val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.equipmentstock)
                val item = Item( 0,args.position, name, description, date, getBitmapAsByteArray(imageBitmap),"install")
                viewModel.addEquipment(item, args.position)
                itemDao.insertAll(item)
            }
        }
        else if (args.itemType == "inhabitant")
        {
            if (viewModel.imageByteArray != null) {
                val item = Item(0,args.position, name, description, date, viewModel.imageByteArray, "adoption")
                viewModel.addInhabitant(item, args.position);
                itemDao.insertAll(item)
            }
            else {
                val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.fishstock)
                val item = Item(0,args.position,name, description, date, getBitmapAsByteArray(imageBitmap), "adoption")
                viewModel.addInhabitant(item, args.position)
                itemDao.insertAll(item)
            }
        }

        return true
    }

    /**
     * pokiaľ sme nezadali všetky potrebné parametre, zobrazení Toast s informáciou
     *
     * @return true pokiaľ sa Toast nezobrazil inak false
     */
    private fun showToast():Boolean {
        if (binding.editTextName.text.toString() == "" && binding.editTextDate.text.toString() == binding.dateText + " date")
        {
            Toast.makeText(requireContext(), "please select name and date", Toast.LENGTH_LONG).show()
            return false
        }
        else if (binding.editTextName.text.toString() == "")
        {
            Toast.makeText(requireContext(), "please select name", Toast.LENGTH_LONG).show()
            return false
        }
        else if (binding.editTextDate.text.toString() == binding.dateText + " date")
        {
            Toast.makeText(requireContext(), "please select date", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

}