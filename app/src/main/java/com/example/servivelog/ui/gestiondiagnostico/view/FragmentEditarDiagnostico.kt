package com.example.servivelog.ui.gestiondiagnostico.view

import android.R
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.servivelog.databinding.FragmentEditarDiagnosticoBinding
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.gestiondiagnostico.adapter.ImageAdapter
import com.example.servivelog.ui.gestiondiagnostico.viewmodel.GestioDiagnosisViewModel
import com.example.servivelog.ui.gestionmantenimiento.adapter.AutoTextLabAdapter
import com.example.servivelog.ui.gestionmantenimiento.adapter.AutotextComptAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


@AndroidEntryPoint
class FragmentEditarDiagnostico : Fragment() {
    private lateinit var editarDiagnosticoBinding: FragmentEditarDiagnosticoBinding
    private lateinit var diagnosisItem: DiagnosisItem
    private val args: FragmentEditarDiagnosticoArgs by navArgs()
    private val gestionDiagnosisViewModel: GestioDiagnosisViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter

    private val bitmapList = mutableListOf<Bitmap>()
    private lateinit var viewPager: ViewPager
    private var ruta1: String? = null
    private var ruta2: String? = null
    private var ruta3: String? = null
    private var ruta4: String? = null
    private val MAX = 4

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentData: Intent? = result.data
                if (intentData != null) {
                    val clipData = intentData.clipData
                    if (clipData != null) {
                        // Multiple images selected
                        for (i in 0 until clipData.itemCount) {
                            val imageUri: Uri = clipData.getItemAt(i).uri
                            handleImageUri(imageUri)
                        }
                    } else {
                        // Single image selected
                        val imageUri: Uri? = intentData.data
                        if (imageUri != null) {
                            handleImageUri(imageUri)
                        }
                    }
                    actualizarConteo(imageAdapter.count)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editarDiagnosticoBinding = FragmentEditarDiagnosticoBinding.inflate(layoutInflater)
        obteniendoDatos()
    }

    private fun obteniendoDatos() {
        diagnosisItem = args.diagnosis
        editarDiagnosticoBinding.ctvLabD.setText(diagnosisItem.nombrelab)
        editarDiagnosticoBinding.ctvServiceTag.setText(diagnosisItem.ServiceTag)
        editarDiagnosticoBinding.etDescripcionDiagnostico.setText(diagnosisItem.descripcion)

        ruta1 = diagnosisItem.ruta1
        ruta2 = diagnosisItem.ruta2
        ruta3 = diagnosisItem.ruta3
        ruta4 = diagnosisItem.ruta4

        // Cargar las imágenes en el adaptador del ViewPager2
        if (ruta1 != null && ruta1!!.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(ruta1)
            bitmap?.let { bitmapList.add(it) }
        }
        if (ruta2 != null && ruta2!!.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(ruta2)
            bitmap?.let { bitmapList.add(it) }
        }
        if (ruta3 != null && ruta3!!.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(ruta3)
            bitmap?.let { bitmapList.add(it) }
        }
        if (ruta4 != null && ruta4!!.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(ruta4)
            bitmap?.let { bitmapList.add(it) }
        }

        viewPager = editarDiagnosticoBinding.vpImagen
        imageAdapter = ImageAdapter(bitmapList, object : ImageAdapter.ImageCountListener {
            override fun onImageAdded(imageCount: Int) {
                // Actualizar el conteo en la UI
                actualizarConteo(imageCount)
            }

            override fun onImageRemoved(imageCount: Int) {
                // Actualizar el conteo en la UI
                actualizarConteo(imageCount)
            }
        })
        viewPager.adapter = imageAdapter
        actualizarConteo(imageAdapter.count)

        editarDiagnosticoBinding.btnFoto.setOnClickListener {
            openGallery()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val btnEditar = editarDiagnosticoBinding.btnEditar

        CoroutineScope(Dispatchers.Main).launch {
            val lab = gestionDiagnosisViewModel.getAllLaboratories()
            setLabAdapter(lab)
        }

        btnEditar.setOnClickListener {
            lifecycleScope.launch {
                val datoL = editarDiagnosticoBinding.ctvLabD.text.toString()
                val datoC = editarDiagnosticoBinding.ctvServiceTag.text.toString()
                val lab = gestionDiagnosisViewModel.getAllLaboratories()
                val labI = lab.filter { it.nombre == datoL }
                val comp = gestionDiagnosisViewModel.getAllComputer()
                val compL = comp.filter { it.ubicacion == datoL }

                if ((labI.isNotEmpty() && labI.size <= 1) && compL.isNotEmpty() && datoL != "" && datoC != ""){
                    diagnosisItem.nombrelab = editarDiagnosticoBinding.ctvLabD.text.toString()
                    diagnosisItem.ServiceTag = editarDiagnosticoBinding.ctvServiceTag.text.toString()
                    diagnosisItem.descripcion = editarDiagnosticoBinding.etDescripcionDiagnostico.text.toString()

                    if (!imageAdapter.hasImageAtPosition(0)) {
                        diagnosisItem.ruta1 = ""
                    }
                    if (!imageAdapter.hasImageAtPosition(1)) {
                        diagnosisItem.ruta2 = ""
                    }
                    if (!imageAdapter.hasImageAtPosition(2)) {
                        diagnosisItem.ruta3 = ""
                    }
                    if (!imageAdapter.hasImageAtPosition(3)) {
                        diagnosisItem.ruta4 = ""
                    }

                    gestionDiagnosisViewModel.updateDiagnosis(diagnosisItem)
                    Toast.makeText(
                        requireContext(),
                        "Diagnóstico editado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    val navController = Navigation.findNavController(requireView())
                    navController.popBackStack()
                }else
                    Toast.makeText(requireContext(), "Hay datos no existentes o falta que ingresen datos", Toast.LENGTH_SHORT).show()
            }
        }

        return editarDiagnosticoBinding.root
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryLauncher.launch(galleryIntent)
    }

    private fun handleImageUri(imageUri: Uri) {
        val imagesTaken = imageAdapter.count
        if (imagesTaken < MAX) {
            val bitmap = getBitmapFromUri(imageUri)
            if (bitmap != null) {
                // Verificar si la imagen ya existe en la lista
                val imageExists = bitmapList.any { it.sameAs(bitmap) }
                if (!imageExists) {
                    // Guardar la ruta de la imagen en la variable correspondiente
                    val imagePath = saveImageToInternalStorage(bitmap)
                    if (imagePath != null) {
                        when (imagesTaken) {
                            0 -> {
                                ruta1 = imagePath
                                diagnosisItem.ruta1 = ruta1 as String
                            }
                            1 -> {
                                ruta2 = imagePath
                                diagnosisItem.ruta2 = ruta2 as String
                            }
                            2 -> {
                                ruta3 = imagePath
                                diagnosisItem.ruta3 = ruta3 as String
                            }
                            3 -> {
                                ruta4 = imagePath
                                diagnosisItem.ruta4 = ruta4 as String
                            }
                        }
                    }
                    // Update the UI with the selected image
                    imageAdapter.addImage(bitmap)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "La imagen ya ha sido seleccionada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Ya has seleccionado el número máximo de imágenes",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): String? {
        val wrapper = ContextWrapper(requireContext().applicationContext)
        var file: File? = null
        try {
            val dir = wrapper.getDir("images", Context.MODE_PRIVATE)
            file = File(dir, "${System.currentTimeMillis()}.jpg")
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file?.absolutePath
    }


    private fun actualizarConteo(imageCount: Int) {
        val countText = "$imageCount/$MAX"
        editarDiagnosticoBinding.conteo.text = countText
    }

    private fun setLabAdapter(lab: List<LabItem>) {
        val adapter = AutoTextLabAdapter(requireContext(), R.layout.simple_dropdown_item_1line, lab.map { it.nombre })
        editarDiagnosticoBinding.ctvLabD.setAdapter(adapter)

        editarDiagnosticoBinding.ctvLabD.setOnItemClickListener { _, _, position, _ ->
            val selectedLab = lab[position]
            CoroutineScope(Dispatchers.Main).launch {
                val computerList = gestionDiagnosisViewModel.getAllComputer().filter { it.ubicacion == selectedLab.nombre }
                setCompAdapter(computerList)
            }
        }
    }

    private fun setCompAdapter(comp: List<ComputerItem>) {
        val adapter = AutotextComptAdapter(requireActivity(), R.layout.simple_dropdown_item_1line ,comp.map { it.serviceTag })
        editarDiagnosticoBinding.ctvServiceTag.setAdapter(adapter)
    }

}

