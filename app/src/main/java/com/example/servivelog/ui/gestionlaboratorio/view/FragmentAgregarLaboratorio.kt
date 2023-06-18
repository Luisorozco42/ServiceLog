package com.example.servivelog.ui.gestionlaboratorio.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.servivelog.databinding.FragmentAgregarLaboratorioBinding
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.gestionlaboratorio.viewmodel.GestionLabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentAgregarLaboratorio : Fragment() {
    private lateinit var agregarLaboratorioBinding: FragmentAgregarLaboratorioBinding
    private val gestionLabViewModel: GestionLabViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agregarLaboratorioBinding = FragmentAgregarLaboratorioBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val btnAgregar = agregarLaboratorioBinding.btnGuardar

        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val labs = gestionLabViewModel.getAllLabs()

                val response = enviarDatos(labs)
                if (response.nombre != ""){
                    gestionLabViewModel.insertLab(response)
                    val navController = Navigation.findNavController(it)
                    navController.popBackStack()
                }else{
                    Toast.makeText(requireContext(), "El laboratorio, ya existe o faltan datos", Toast.LENGTH_SHORT).show()
                }

            }
        }

        return agregarLaboratorioBinding.root
    }

    private fun enviarDatos(labs: List<LabItem>): InsertLab {

        val lab = labs.filter { it.nombre == agregarLaboratorioBinding.NombreLabs.text.toString() }
        val nombreL = agregarLaboratorioBinding.NombreLabs.text.toString()
        val desc = agregarLaboratorioBinding.etDescripcionLabs.text.toString()

        if (lab.isNotEmpty() || nombreL.isEmpty() || desc.isEmpty()) {
            return InsertLab("", "")
        } else
            return InsertLab(
                nombre = agregarLaboratorioBinding.NombreLabs.text.toString(),
                descripcion = agregarLaboratorioBinding.etDescripcionLabs.text.toString()
            )
    }


}