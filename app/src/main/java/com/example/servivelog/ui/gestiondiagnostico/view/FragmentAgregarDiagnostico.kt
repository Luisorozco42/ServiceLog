package com.example.servivelog.ui.gestiondiagnostico.view

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.example.servivelog.databinding.FragmentAgregarDiagnosticoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentAgregarDiagnostico : Fragment() {
    private var MAX = 4
    private var imagesTaken = 0
    private val bitmapList = mutableListOf<Bitmap>()

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var viewPager: ViewPager
    private lateinit var imageCountTextView: TextView
    private val gestionDiagnosisViewModel: GestioDiagnosisViewModel by viewModels()
    private lateinit var agregarDiagnosticoBinding: FragmentAgregarDiagnosticoBinding

    // Variables para las rutas de las imágenes
    private var ruta1: String? = null
    private var ruta2: String? = null
    private var ruta3: String? = null
    private var ruta4: String? = null

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
                    actualizarConteo()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        agregarDiagnosticoBinding =
            FragmentAgregarDiagnosticoBinding.inflate(inflater, container, false)

        val btnAgregar = agregarDiagnosticoBinding.btnGuardar
        val btnFoto = agregarDiagnosticoBinding.btnFoto

        CoroutineScope(Dispatchers.Main).launch {
            val lab = gestionDiagnosisViewModel.getAllLaboratories()
            setLabAdapter(lab)
        }

        // Inicializar ViewPager y ImageAdapter
        viewPager = agregarDiagnosticoBinding.viewPager
        imageAdapter = ImageAdapter(mutableListOf(), object : ImageAdapter.ImageCountListener {
            override fun onImageAdded(imageCount: Int) {
                // Lógica para cuando se agrega una imagen
                // Actualizar el conteo en la UI
                actualizarConteo()
            }

            override fun onImageRemoved(imageCount: Int) {
                // Lógica para cuando se elimina una imagen
                // Actualizar el conteo en la UI
                actualizarConteo()
            }


        })
        viewPager.adapter = imageAdapter
        imageCountTextView = agregarDiagnosticoBinding.conteo
        actualizarConteo()


        btnAgregar.setOnClickListener {
            agregarDatos(it)
        }


        btnFoto.setOnClickListener {
            openGallery()

        }

        return agregarDiagnosticoBinding.root
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryLauncher.launch(galleryIntent)
    }

    private fun handleImageUri(imageUri: Uri) {
        if (imagesTaken < MAX) {
            imagesTaken += 1 // Incrementar después de agregar la imagen

            val bitmap = getBitmapFromUri(imageUri)
            if (bitmap != null) {
                // Verificar si la imagen ya existe en la lista
                val imageExists = bitmapList.any { it.sameAs(bitmap) }
                if (!imageExists) {
                    bitmapList.add(bitmap)

                    // Guardar la ruta de la imagen en la variable correspondiente
                    when (imagesTaken) {
                        1 -> {
                            ruta1 = saveImageToInternalStorage(bitmap)
                            imageAdapter.addImage(bitmap)
                        }

                        2 -> {
                            ruta2 = saveImageToInternalStorage(bitmap)
                            imageAdapter.addImage(bitmap)
                        }

                        3 -> {
                            ruta3 = saveImageToInternalStorage(bitmap)
                            imageAdapter.addImage(bitmap)
                        }

                        4 -> {
                            ruta4 = saveImageToInternalStorage(bitmap)
                            imageAdapter.addImage(bitmap)
                        }
                    }

                    // Update the UI with the selected images
                    imageAdapter.notifyDataSetChanged()
                    actualizarConteo()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "La imagen ya ha sido seleccionada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se pudo cargar la imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Ya has seleccionado el número máximo de imágenes",
                Toast.LENGTH_SHORT
            ).show()
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


    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun agregarDatos(view: View) {

        lifecycleScope.launch {
            val datoL = agregarDiagnosticoBinding.ctvLabD.text.toString()
            val datoC = agregarDiagnosticoBinding.ctvServiceTag.text.toString()
            val lab = gestionDiagnosisViewModel.getAllLaboratories()
            val labI = lab.filter { it.nombre == datoL }
            val comp = gestionDiagnosisViewModel.getAllComputer()
            val compL = comp.filter { it.ubicacion == datoL }
            if (labI.isNotEmpty()  && compL.isNotEmpty() && datoL != "" && datoC != ""){
                val laboratorio = datoL
                val servicio = datoC
                val descripcion = agregarDiagnosticoBinding.etDescripcionDiagnostico.text.toString()

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = Date()
                val formatedDate = dateFormat.format(date)

                val insertDiagnosis = InsertDiagnosis(
                    nombrelab = laboratorio,
                    ServiceTag = servicio,
                    descripcion = descripcion,
                    ruta1 = ruta1 ?: "",
                    ruta2 = ruta2 ?: "",
                    ruta3 = ruta3 ?: "",
                    ruta4 = ruta4 ?: "",
                    fecha = formatedDate
                )

                gestionDiagnosisViewModel.insertDiagnosi(insertDiagnosis)
                Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT)
                    .show()

                val navController = Navigation.findNavController(view)
                navController.popBackStack()

            }else
                Toast.makeText(requireContext(), "Hay datos no existentes o falta que ingresen datos", Toast.LENGTH_SHORT).show()
        }
    }


    private var previousImageCount: Int = 0


    private fun actualizarConteo() {
        // Verificar si el adaptador es una instancia de ImageAdapter
        if (imageAdapter is ImageAdapter) {
            val imageCount = imageAdapter.getCount()
            // Hacer algo con el número de imágenes
            val countText = "$imageCount/$MAX"
            imageCountTextView.text = countText
            agregarDiagnosticoBinding.btnFoto.visibility =
                if (imageCount == MAX) View.GONE else View.VISIBLE

            // Verificar si se eliminó una imagen
            if (imageCount < previousImageCount) {
                // Realizar acciones después de eliminar una imagen
                Toast.makeText(requireContext(), "Imagen eliminada", Toast.LENGTH_SHORT).show()
                // Otros pasos que deseas realizar
            }


            // Guardar el número de imágenes actual para la próxima actualización
            previousImageCount = imageCount
        }
    }

    private fun setLabAdapter(lab: List<LabItem>) {
        val adapter = AutoTextLabAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, lab.map { it.nombre })
        agregarDiagnosticoBinding.ctvLabD.setAdapter(adapter)

        agregarDiagnosticoBinding.ctvLabD.setOnItemClickListener { _, _, position, _ ->
            val selectedLab = lab[position]
            CoroutineScope(Dispatchers.Main).launch {
                val computerList = gestionDiagnosisViewModel.getAllComputer().filter { it.ubicacion == selectedLab.nombre }
                setCompAdapter(computerList)
            }
        }
    }

    private fun setCompAdapter(comp: List<ComputerItem>) {
        val adapter = AutotextComptAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line ,comp.map { it.serviceTag })
        agregarDiagnosticoBinding.ctvServiceTag.setAdapter(adapter)
    }

}
