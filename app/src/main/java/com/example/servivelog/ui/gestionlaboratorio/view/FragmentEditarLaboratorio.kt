package com.example.servivelog.ui.gestionlaboratorio.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.servivelog.databinding.FragmentEditarLaboratorioBinding
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.gestionlaboratorio.viewmodel.GestionLabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentEditarLaboratorio : Fragment() {
    private lateinit var editarLaboratoriosBinding: FragmentEditarLaboratorioBinding
    private lateinit var labItem: LabItem
    private val args: FragmentEditarLaboratorioArgs by navArgs()
    private val gestionLabViewModel: GestionLabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        editarLaboratoriosBinding = FragmentEditarLaboratorioBinding.inflate(layoutInflater)
        obteniendoDatos()
        super.onCreate(savedInstanceState)

    }

    private fun obteniendoDatos() {
        labItem = args.lab
        editarLaboratoriosBinding.NombreLabs.setText(labItem.nombre)
        editarLaboratoriosBinding.etDescripcionLabs.setText(labItem.descripcion)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val btnEditar = editarLaboratoriosBinding.btnEditar

        btnEditar.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                val labs = gestionLabViewModel.getAllLabs()
                val nombreLab = editarLaboratoriosBinding.NombreLabs.text.toString()
                val desc = editarLaboratoriosBinding.etDescripcionLabs.text.toString()
                val encontrado = labs.filter { it.nombre == nombreLab }

                if (nombreLab.isNotEmpty() && desc.isNotEmpty()) {
                    if ((encontrado.isNotEmpty() && nombreLab == args.lab.nombre) || encontrado.isEmpty()) {
                        labItem.nombre = editarLaboratoriosBinding.NombreLabs.text.toString()
                        labItem.descripcion = editarLaboratoriosBinding.etDescripcionLabs.text.toString()
                        gestionLabViewModel.updateLab(labItem)
                        val navController = Navigation.findNavController(it)
                        navController.popBackStack()
                    } else
                        Toast.makeText(
                            requireContext(),
                            "El laboratorio ya existe",
                            Toast.LENGTH_SHORT
                        ).show()
                } else
                    Toast.makeText(requireContext(), "Hay casillas vacías", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        return editarLaboratoriosBinding.root
    }


}