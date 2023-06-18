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
        val agregarBtn = agregarComputadoraBinding.btnAgregar
        agregarBtn.setOnClickListener {
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

        val nombre = agregarComputadoraBinding.etNombre.text.toString()
        val descripcion = agregarComputadoraBinding.etDescripcion.text.toString()
        val marca = agregarComputadoraBinding.etMarca.text.toString()
        val modelo = agregarComputadoraBinding.etModelo.text.toString()
        val procesador = agregarComputadoraBinding.etProcesador.text.toString()
        val ram = agregarComputadoraBinding.etRam.text.toString().toIntOrNull()
        val almacenamiento = agregarComputadoraBinding.etAlmacenamiento.text.toString().toIntOrNull()
        val serviceTag = agregarComputadoraBinding.etServiceTag.text.toString()
        val noInventario = agregarComputadoraBinding.etInventario.text.toString()
        val ubicacion = agregarComputadoraBinding.etUbicacion.text.toString()
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