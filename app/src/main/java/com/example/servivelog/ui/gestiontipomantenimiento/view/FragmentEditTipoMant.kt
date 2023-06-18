package com.example.servivelog.ui.gestiontipomantenimiento.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.servivelog.databinding.FragmentEditTipoMantBinding
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.ui.gestiontipomantenimiento.viewmodel.GestionTipoMantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentEditTipoMant : Fragment() {

    private lateinit var fragmentEditTipoMantBinding: FragmentEditTipoMantBinding
    private val args: FragmentEditTipoMantArgs by navArgs()
    private lateinit var tipoMantItem: TipoMantItem
    private val gestionTipoMantViewModel: GestionTipoMantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentEditTipoMantBinding = FragmentEditTipoMantBinding.inflate(layoutInflater)
        obteniendoDatos()
        super.onCreate(savedInstanceState)
    }

    private fun obteniendoDatos() {
        tipoMantItem = args.tipoMant
        fragmentEditTipoMantBinding.NombreLabs.setText(tipoMantItem.nombre)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEditTipoMantBinding.btnGuardar.setOnClickListener{
            actualizarDatos(it)
        }
        return fragmentEditTipoMantBinding.root
    }

    private fun actualizarDatos(view: View) {
        gestionTipoMantViewModel.onCreate()
        gestionTipoMantViewModel.modeloTipoMant.observe(viewLifecycleOwner){
            val lista = it.filter { it.nombre.uppercase() == fragmentEditTipoMantBinding.NombreLabs.text.toString().uppercase() }

            if (lista.isEmpty()){
                if (fragmentEditTipoMantBinding.NombreLabs.text.toString() != ""){
                    tipoMantItem.nombre = fragmentEditTipoMantBinding.NombreLabs.text.toString()
                    gestionTipoMantViewModel.updateTipoMant(tipoMantItem)
                    val navController = Navigation.findNavController(view)
                    navController.popBackStack()
                }else
                    Toast.makeText(requireContext(), "La casilla de tipo datos se encuentra vacía", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(requireContext(), "El tipo de mantenimineto ya existe", Toast.LENGTH_SHORT).show()
        }
    }

}