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
import com.example.servivelog.databinding.FragmentEditarComputadoraBinding
import com.example.servivelog.domain.computerusecase.RUDComputer
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

        btnEditarComputadora.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val comp = gestionCompViewModel.getComputers()
                val nombre = editarComputadoraBinding.etNombre.text.toString()
                val descripcion = editarComputadoraBinding.etDescripcion.text.toString()
                val marca = editarComputadoraBinding.etMarca.text.toString()
                val modelo = editarComputadoraBinding.etModelo.text.toString()
                val procesador = editarComputadoraBinding.etProcesador.text.toString()
                val ram = editarComputadoraBinding.etRam.text.toString().toIntOrNull()
                val almacenamiento =
                    editarComputadoraBinding.etAlmacenamiento.text.toString().toIntOrNull()
                val serviceTag = editarComputadoraBinding.etServiceTag.text.toString()
                val noInventario = editarComputadoraBinding.etInventario.text.toString()
                val serviceTagE = comp.filter { it.serviceTag == serviceTag }
                val noInventarioE = comp.filter { it.noInventario == noInventario }

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