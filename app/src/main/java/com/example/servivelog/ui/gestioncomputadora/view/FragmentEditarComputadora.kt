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
import com.example.servivelog.databinding.FragmentEditarComputadoraBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.ui.gestioncomputadora.viewmodel.GestionCompViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentEditarComputadora : Fragment() {

    private lateinit var editarComputadoraBinding: FragmentEditarComputadoraBinding
    private lateinit var computerItem: ComputerItem
    private val args: FragmentEditarComputadoraArgs by navArgs()
    private val gestionCompViewModel: GestionCompViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        editarComputadoraBinding = FragmentEditarComputadoraBinding.inflate(layoutInflater)
        obteniendoDatos()
        editarComputadoraBinding.etUbicacion.isEnabled = false
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val btnEditarComputadora = editarComputadoraBinding.btnEditar
        val tilNombre = editarComputadoraBinding.tilNombre
        val nombreText = editarComputadoraBinding.etNombre
        val tilDesc = editarComputadoraBinding.tilDesc
        val descText = editarComputadoraBinding.etDescripcion
        val tilMarca = editarComputadoraBinding.tilMarca
        val marcaText = editarComputadoraBinding.etMarca
        val tilModelo = editarComputadoraBinding.tilModelo
        val modeloText = editarComputadoraBinding.etModelo
        val tilprocesador = editarComputadoraBinding.tilProcesador
        val procesadorText = editarComputadoraBinding.etProcesador
        val tilRam = editarComputadoraBinding.tilRam
        val ramText = editarComputadoraBinding.etRam
        val tilAlmacenamiento = editarComputadoraBinding.tilAlmacenamiento
        val almacenamientoText = editarComputadoraBinding.etAlmacenamiento
        val tilServiceTag = editarComputadoraBinding.tilServiceTag
        val serviceTagText = editarComputadoraBinding.etServiceTag
        val tilNoInventario = editarComputadoraBinding.tilInventario
        val noInventarioText = editarComputadoraBinding.etInventario

        nombreText.onTextChanged { tilNombre.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        descText.onTextChanged { tilDesc.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        marcaText.onTextChanged { tilMarca.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        modeloText.onTextChanged { tilModelo.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        procesadorText.onTextChanged { tilprocesador.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        ramText.onTextChanged { tilRam.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        almacenamientoText.onTextChanged { tilAlmacenamiento.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        serviceTagText.onTextChanged { tilServiceTag.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }
        noInventarioText.onTextChanged { tilNoInventario.setErrorIfInvalid(it,"La información ingresada no es válida", "El campo está vacío, ") }

        btnEditarComputadora.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val comp = gestionCompViewModel.getComputers()
                val nombre = editarComputadoraBinding.etNombre.text.toString().trim()
                val descripcion = editarComputadoraBinding.etDescripcion.text.toString().trim()
                val marca = editarComputadoraBinding.etMarca.text.toString().trim()
                val modelo = editarComputadoraBinding.etModelo.text.toString().trim()
                val procesador = editarComputadoraBinding.etProcesador.text.toString().trim()
                val ram = editarComputadoraBinding.etRam.text.toString().trim().toIntOrNull()
                val almacenamiento =
                    editarComputadoraBinding.etAlmacenamiento.text.toString().trim().toIntOrNull()
                val serviceTag = editarComputadoraBinding.etServiceTag.text.toString().trim()
                val noInventario = editarComputadoraBinding.etInventario.text.toString().trim()
                val serviceTagE = comp.filter { it.serviceTag == serviceTag }

                if (descripcion.isEmpty() || marca.isEmpty() || modelo.isEmpty() || procesador.isEmpty() || ram == null || almacenamiento == null
                    || serviceTag.isEmpty() || noInventario.isEmpty() || nombre.isEmpty() ||
                    (serviceTagE.isNotEmpty() && serviceTag != args.computer.serviceTag) || (noInventario.isNotEmpty() && noInventario != args.computer.noInventario)
                ) {
                    Toast.makeText(requireContext(), "Faltan datos, el serviceTag o el No.Inventario se repite el laboratorio ingresado no existe", Toast.LENGTH_SHORT).show()
                }else{
                    computerItem.ram = editarComputadoraBinding.etRam.text.toString().toInt()
                    computerItem.procesador = editarComputadoraBinding.etProcesador.text.toString()
                    computerItem.serviceTag = editarComputadoraBinding.etServiceTag.text.toString()
                    computerItem.marca = editarComputadoraBinding.etMarca.text.toString()
                    computerItem.modelo = editarComputadoraBinding.etModelo.text.toString()
                    computerItem.nombre = editarComputadoraBinding.etNombre.text.toString()
                    computerItem.ubicacion = editarComputadoraBinding.etUbicacion.text.toString()
                    computerItem.noInventario = editarComputadoraBinding.etInventario.text.toString()
                    computerItem.descripcion = editarComputadoraBinding.etDescripcion.text.toString()
                    computerItem.almacenamiento =
                        editarComputadoraBinding.etAlmacenamiento.text.toString().toInt()

                    gestionCompViewModel.updateComputer(computerItem)

                    val navController = Navigation.findNavController(it)
                    navController.popBackStack()
                }
            }
        }
        return editarComputadoraBinding.root
    }

    private fun obteniendoDatos() {
        computerItem = args.computer
        editarComputadoraBinding.etNombre.setText(computerItem.nombre)
        editarComputadoraBinding.etAlmacenamiento.setText(computerItem.almacenamiento.toString())
        editarComputadoraBinding.etDescripcion.setText(computerItem.descripcion)
        editarComputadoraBinding.etMarca.setText(computerItem.marca)
        editarComputadoraBinding.etModelo.setText(computerItem.modelo)
        editarComputadoraBinding.etInventario.setText(computerItem.noInventario)
        editarComputadoraBinding.etServiceTag.setText(computerItem.serviceTag)
        editarComputadoraBinding.etUbicacion.setText(computerItem.ubicacion)
        editarComputadoraBinding.etRam.setText(computerItem.ram.toString())
        editarComputadoraBinding.etProcesador.setText(computerItem.procesador)
    }
}