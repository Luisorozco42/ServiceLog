package com.example.servivelog.ui.gestioncomputadora.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.servivelog.core.ex.onTextChanged
import com.example.servivelog.core.ex.setErrorIfInvalid
import com.example.servivelog.databinding.FragmentAgregarComputadoraBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.computer.InsertItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.gestioncomputadora.viewmodel.GestionCompViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentAgregarComputadora : Fragment() {

    private lateinit var agregarComputadoraBinding: FragmentAgregarComputadoraBinding
    private val gestionCompViewModel: GestionCompViewModel by viewModels()
    private val args: FragmentAgregarComputadoraArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        agregarComputadoraBinding = FragmentAgregarComputadoraBinding.inflate(layoutInflater)
        agregarComputadoraBinding.etUbicacion.setText(args.ubicacion)
        agregarComputadoraBinding.etUbicacion.isEnabled = false
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val tilNombre = agregarComputadoraBinding.tilNombre
        val nombreText = agregarComputadoraBinding.etNombre
        val tilDesc = agregarComputadoraBinding.tilDescripcion
        val descText = agregarComputadoraBinding.etDescripcion
        val tilMarca = agregarComputadoraBinding.tilMarca
        val marcaText = agregarComputadoraBinding.etMarca
        val tilModelo = agregarComputadoraBinding.tilModelo
        val modeloText = agregarComputadoraBinding.etModelo
        val tilprocesador = agregarComputadoraBinding.tilProcesadord
        val procesadorText = agregarComputadoraBinding.etProcesador
        val tilRam = agregarComputadoraBinding.tilRam
        val ramText = agregarComputadoraBinding.etRam
        val tilAlmacenamiento = agregarComputadoraBinding.tilAlmacenamiento
        val almacenamientoText = agregarComputadoraBinding.etAlmacenamiento
        val tilServiceTag = agregarComputadoraBinding.tilServiceTag
        val serviceTagText = agregarComputadoraBinding.etServiceTag
        val tilNoInventario = agregarComputadoraBinding.tilNoInventario
        val noInventarioText = agregarComputadoraBinding.etInventario
        val agregarBtn = agregarComputadoraBinding.btnAgregar

        nombreText.onTextChanged { tilNombre.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        descText.onTextChanged { tilDesc.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        marcaText.onTextChanged { tilMarca.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        modeloText.onTextChanged { tilModelo.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        procesadorText.onTextChanged { tilprocesador.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        ramText.onTextChanged { tilRam.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        almacenamientoText.onTextChanged { tilAlmacenamiento.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        serviceTagText.onTextChanged { tilServiceTag.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }
        noInventarioText.onTextChanged { tilNoInventario.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío") }

        agregarBtn.setOnClickListener {

            tilNombre.setErrorIfInvalid(nombreText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            tilDesc.setErrorIfInvalid(descText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilMarca.setErrorIfInvalid(marcaText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilModelo.setErrorIfInvalid(modeloText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilprocesador.setErrorIfInvalid(procesadorText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilRam.setErrorIfInvalid(ramText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilAlmacenamiento.setErrorIfInvalid(almacenamientoText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilServiceTag.setErrorIfInvalid(serviceTagText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            tilNoInventario.setErrorIfInvalid(noInventarioText.text.toString(),"La información ingresada no es válida", "El campo está vacío")

            CoroutineScope(Dispatchers.Main).launch {
                val labItem = gestionCompViewModel.getAllLabs()
                val comp = gestionCompViewModel.getComputers()
                val insertItem = enviarDatos(labItem, comp)
                if (insertItem.ubicacion == "")
                    Toast.makeText(requireContext(), "Faltan datos, el serviceTag o el No.Inventario se repite el laboratorio ingresado no existe", Toast.LENGTH_SHORT).show()
                else{
                    gestionCompViewModel.insertComputer(insertItem)
                    val navController = Navigation.findNavController(it)
                    navController.popBackStack()//popbackstack te devuelve al fragment anterior y cierra este
                }
            }

        }
        return agregarComputadoraBinding.root
    }

    private fun enviarDatos(labs: List<LabItem>, comp: List<ComputerItem>): InsertItem {

        val nombre = agregarComputadoraBinding.etNombre.text.toString().trim()
        val descripcion = agregarComputadoraBinding.etDescripcion.text.toString().trim()
        val marca = agregarComputadoraBinding.etMarca.text.toString().trim()
        val modelo = agregarComputadoraBinding.etModelo.text.toString().trim()
        val procesador = agregarComputadoraBinding.etProcesador.text.toString().trim()
        val ram = agregarComputadoraBinding.etRam.text.toString().trim().toIntOrNull()
        val almacenamiento = agregarComputadoraBinding.etAlmacenamiento.text.toString().trim().toIntOrNull()
        val serviceTag = agregarComputadoraBinding.etServiceTag.text.toString().trim()
        val noInventario = agregarComputadoraBinding.etInventario.text.toString().trim()
        val ubicacion = agregarComputadoraBinding.etUbicacion.text.toString().trim()
        val serviceTagNoInventario = comp.filter { it.serviceTag == serviceTag || it.noInventario == noInventario}

        if (descripcion.isEmpty() || marca.isEmpty() || modelo.isEmpty() || procesador.isEmpty() || ram == null || almacenamiento == null
            || serviceTag.isEmpty() || noInventario.isEmpty() || serviceTagNoInventario.isNotEmpty() || nombre.isEmpty()){
            return InsertItem("", "", "", "", "", 0, 0, "", "", "")
        }else{
            return InsertItem(
                nombre = nombre, descripcion = descripcion, marca = marca, modelo = modelo, procesador = procesador, ram = ram, almacenamiento = almacenamiento, serviceTag = serviceTag, noInventario = noInventario, ubicacion = ubicacion
            )
        }
    }
}