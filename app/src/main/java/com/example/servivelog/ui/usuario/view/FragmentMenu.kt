package com.example.servivelog.ui.usuario.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentMenuBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.usuario.adapters.DashboardD
import com.example.servivelog.ui.usuario.adapters.DashboardM
import com.example.servivelog.ui.usuario.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentMenu : Fragment() {

    private lateinit var menuBinding: FragmentMenuBinding
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBinding = FragmentMenuBinding.inflate(layoutInflater)
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

        CoroutineScope(Dispatchers.Main).launch {

            val todomantenimiento = userViewModel.getAllmantenimiento()
            val tododiagnostico = userViewModel.getAllDiagnosis()
            val cantidad = obtenerlast4Month(todomantenimiento)

            menuBinding.cantidad.text =
                cantidad.toString()//te devuelve la cantidad de mantenimiento creados  en los ultimos 4 meses
            val cantidadD = obtenerlastMonth(tododiagnostico)
            menuBinding.cantidadD.text = cantidadD.toString()
        }
        return menuBinding.root
    }

    private fun setdashboardD(listad: List<DiagnosisItem>) {
        val recyclerView = menuBinding.ultimosDiagnosticos
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = DashboardD(requireActivity(),listad, menuBinding.root)
    }

    private fun setdashboardM(listam: List<MantenimientoCUDItem>) {//crea el dashboard
        val recyclerView = menuBinding.ultimosMantenimiento
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter =
            DashboardM(requireActivity(), listam, menuBinding.root)//muestra las ultimas computadoras , creadas en los ultmos 4 meses
    }

    private fun obtenerlast4Month(listaM: List<MantenimientoCUDItem>): Int {//fun para obtener los 4 meses
        val formato = SimpleDateFormat("yyyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val fechaActual = Date()
        calendar.time = fechaActual
        calendar.add(Calendar.MONTH, -4)

        val fechaLimite = calendar.time
        var contador = 0
        var last4MM: MutableList<MantenimientoCUDItem> = mutableListOf()

        for (m in listaM) {
            val fecha = formato.parse(m.dia)
            if (fecha.after(fechaLimite) || fecha == fechaLimite){
                contador++
                last4MM.add(m)
            }
        }
        setdashboardM(last4MM.subList(0, minOf(last4MM.size, 4)))
        return contador
    }
    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Terminar la sesión")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
                false //esto funciona para dejar de mostrar el navigation view despues de cerrar sesion
            val navController = Navigation.findNavController(menuBinding.root)
            navController.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        val dialog = alertDialogBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.GREEN)
    }

    private fun obtenerlastMonth(listaD: List<DiagnosisItem>): Int{
        val formato = SimpleDateFormat("yyyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val fechaActual = Date()
        calendar.time = fechaActual
        calendar.add(Calendar.MONTH, -1)

        val fechaLimite = calendar.time
        var contador = 0
        val lastMD: MutableList<DiagnosisItem> = mutableListOf()

        for (d in listaD) {
            val fecha = formato.parse(d.fecha)
            if (fecha.after(fechaLimite) || fecha == fechaLimite ) {
                contador++
                lastMD.add(d)
            }
        }

        setdashboardD(lastMD.subList(0, minOf(lastMD.size, 4)))
        return contador

    }

}