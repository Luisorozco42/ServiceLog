package com.example.servivelog.ui.gestiontipomantenimiento.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.servivelog.core.ex.onTextChanged
import com.example.servivelog.core.ex.setErrorIfInvalid
import com.example.servivelog.databinding.FragmentAgregarTipoMantBinding
import com.example.servivelog.domain.model.tipoMantenimiento.InsertTipoMant
import com.example.servivelog.ui.gestiontipomantenimiento.viewmodel.GestionTipoMantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAgregarTipoMant : Fragment() {

    private lateinit var agregarTipoMantBinding: FragmentAgregarTipoMantBinding
    private val gestionTipoMantViewModel: GestionTipoMantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        agregarTipoMantBinding = FragmentAgregarTipoMantBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val tilTipoMant = agregarTipoMantBinding.tilTipoMant
        val tipoMantText = agregarTipoMantBinding.etTipoMant

        tipoMantText.onTextChanged { tilTipoMant.setErrorIfInvalid(it, "La información ingresada no es válida", "El campo está vacío") }

        agregarTipoMantBinding.btnGuardar.setOnClickListener {
            tilTipoMant.setErrorIfInvalid(tipoMantText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            enviarDatos(it)
        }

        return agregarTipoMantBinding.root
    }

    private fun enviarDatos(view: View) {

        gestionTipoMantViewModel.onCreate()
        gestionTipoMantViewModel.modeloTipoMant.observe(viewLifecycleOwner) {
            val tipoMant = it.find { it.nombre == agregarTipoMantBinding.etTipoMant.text.toString() }
            if (agregarTipoMantBinding.etTipoMant.text.toString() != "") {
                if (tipoMant == null) {
                    gestionTipoMantViewModel.insertTipoMant(InsertTipoMant(agregarTipoMantBinding.etTipoMant.text.toString()))
                    val navController = Navigation.findNavController(view)
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se puede agregar un tipo de mantenimiento que tenga el mismo nombre que otro",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Debe datos para agregar el tipo de mantenimiento",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}