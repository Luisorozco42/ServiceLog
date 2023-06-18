package com.example.servivelog.ui.gestionlaboratorio.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentLaboratoriosBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.gestionlaboratorio.LabAdapter
import com.example.servivelog.ui.gestionlaboratorio.viewmodel.GestionLabViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentLaboratorios : Fragment(), LabAdapter.OnDeleteClickListener {

    private lateinit var laboratorioBinding: FragmentLaboratoriosBinding

    private val gestionLabViewModel: GestionLabViewModel by viewModels()
    private lateinit var addbtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LabAdapter
    private var labf: List<LabItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        laboratorioBinding = FragmentLaboratoriosBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Mostrar el cuadro de diálogo de confirmación aquí
            showExitConfirmationDialog()
        }
        callback.isEnabled = true

        addbtn = laboratorioBinding.fbtnAdd

        cargarDatos()

        addbtn.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_Fragmentlaboratorios_to_fragmentAgregarLaboratorio)
        }

        return laboratorioBinding.root
    }

    private fun cargarDatos() {
        gestionLabViewModel.onCreate()
        gestionLabViewModel.modeloLab.observe(viewLifecycleOwner) { lab ->
            labf = lab
            setAdapter(labf)

            laboratorioBinding.etLab.addTextChangedListener { filter ->
                val labfiltrados =
                    labf.filter { it.nombre.uppercase().contains(filter.toString().uppercase()) }
                adapter.updateRecycler(labfiltrados)
            }
        }
    }

    override fun onDeleteClicked(labItem: LabItem) {
        val updatedList = adapter.listL.toMutableList()
        updatedList.remove(labItem)
        labf = updatedList
        adapter.updateRecycler(updatedList)
    }

    private fun setAdapter(it: List<LabItem>) {
        adapter = LabAdapter(requireActivity(), it, laboratorioBinding.root, gestionLabViewModel)
        adapter.setOnDeleteClickListener(this)
        recyclerView = laboratorioBinding.rvLab
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Terminar la sesión")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
                false //esto funciona para dejar de mostrar el navigation view despues de cerrar sesion
            val navController = Navigation.findNavController(laboratorioBinding.root)
            navController.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        val dialog = alertDialogBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.GREEN)
    }

}