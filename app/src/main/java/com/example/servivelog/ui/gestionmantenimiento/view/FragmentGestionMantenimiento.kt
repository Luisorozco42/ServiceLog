package com.example.servivelog.ui.gestionmantenimiento.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentGestionMantenimientoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.gestionmantenimiento.adapter.MantenimientoAdapter
import com.example.servivelog.ui.gestionmantenimiento.viewmodel.GestionManteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class FragmentGestionMantenimiento : Fragment(), MantenimientoAdapter.OnDeleteClickListener {

    private val gestionManteViewModel: GestionManteViewModel by viewModels()
    private lateinit var gestionMantenimientoBinding: FragmentGestionMantenimientoBinding
    private lateinit var addBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private var adapter: MantenimientoAdapter? = null
    private var mantf: List<MantenimientoCUDItem> = emptyList()
    private var filteredList: List<MantenimientoCUDItem> = emptyList()
    private var selectedLaboratory: String = "Seleccionar"
    private var selectedDateS: String = ""
    private var serviceTag: String = ""
    private var selectedDateF: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        gestionMantenimientoBinding = FragmentGestionMantenimientoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gestionManteViewModel.onCreate()

        gestionManteViewModel.modeloMantenimiento.observe(viewLifecycleOwner) { mantenimiento ->

            mantf = mantenimiento

            CoroutineScope(Dispatchers.Main).launch {
                val listac = gestionManteViewModel.getAllComputers()
                setAdapter(mantf, listac)
                gestionMantenimientoBinding.etFechaIn.setText("yyyy-MM-dd")
                gestionMantenimientoBinding.etFechaFin.setText("yyyy-MM-dd")
            }

            gestionMantenimientoBinding.etServiceTag.addTextChangedListener { filter ->

                selectedLaboratory = gestionMantenimientoBinding.spinner.selectedItem.toString()
                selectedDateS = gestionMantenimientoBinding.etFechaIn.text.toString()
                selectedDateF = gestionMantenimientoBinding.etFechaFin.text.toString()

                applyFilters(selectedLaboratory, selectedDateS, filter.toString(), selectedDateF)
            }
        }

        gestionMantenimientoBinding.etFechaIn.setOnClickListener { showDatePickerDialog() }
        gestionMantenimientoBinding.etFechaFin.setOnClickListener { showDatePickerDialog2() }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Mostrar el cuadro de diálogo de confirmación aquí
            showExitConfirmationDialog()
        }
        callback.isEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            val labs = gestionManteViewModel.getAllLabs()
            val listaNombres: ArrayList<String> = ArrayList()

            listaNombres.add("Seleccionar")
            for (l in labs) {
                val nombre = l.nombre
                listaNombres.add(nombre)
            }

            setAdapterSpinner(listaNombres)
        }

        addBtn = gestionMantenimientoBinding.fbtnagregar

        addBtn.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentGestionMantenimiento_to_fragmentAgregarMantenimiento)
        }

        gestionMantenimientoBinding.btnClean.setOnClickListener{
            gestionMantenimientoBinding.etFechaIn.setText("yyyy-MM-dd")
            gestionMantenimientoBinding.etFechaFin.setText("yyyy-MM-dd")
            gestionMantenimientoBinding.etServiceTag.setText("")
            gestionMantenimientoBinding.spinner.setSelection(0)
        }

        return gestionMantenimientoBinding.root
    }

    private fun setAdapterSpinner(listaNombres: ArrayList<String>) {
        val spinner = gestionMantenimientoBinding.spinner
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaNombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String

                serviceTag = gestionMantenimientoBinding.etServiceTag.text.toString()
                selectedDateS = gestionMantenimientoBinding.etFechaIn.text.toString()
                selectedDateF = gestionMantenimientoBinding.etFechaFin.text.toString()

                applyFilters(selectedItem, selectedDateS, serviceTag, selectedDateF)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Lógica cuando no se selecciona ningún elemento
            }
        }
    }

    override fun onDeleteClicked(mantenimientoCUDItem: MantenimientoCUDItem) {
        val savingData: MutableList<MantenimientoCUDItem> = mantf.toMutableList() //Se guarda mantf para no perder la lista original
        savingData.remove(mantenimientoCUDItem)//Se elimina el dato del recycler
        val updatedList = adapter?.listM?.toMutableList()//Esta es la lista filtrada
        updatedList?.remove(mantenimientoCUDItem)//se elimina el dato de la lista filtrada
        mantf = updatedList!!//creo que no es necesario
        filteredList = filteredList.filter { it != mantenimientoCUDItem } //se filtra la lista
        adapter?.updateRecycler(filteredList)// se muestra
        mantf = savingData.toList()//lista recuperada
    }

    private fun setAdapter(it: List<MantenimientoCUDItem>, listac: List<ComputerItem>) {
        adapter = MantenimientoAdapter(
            requireActivity(),
            requireActivity(),
            it,
            gestionMantenimientoBinding.root,
            gestionManteViewModel,
            listac
        )
        adapter?.setOnDeleteClickListener(this)
        recyclerView = gestionMantenimientoBinding.rvMantenimiento
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
            val navController = Navigation.findNavController(gestionMantenimientoBinding.root)
            navController.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        val dialog = alertDialogBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.GREEN)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), R.style.MyDatePickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                gestionMantenimientoBinding.etFechaIn.setText(formattedDate)

                serviceTag = gestionMantenimientoBinding.etServiceTag.text.toString()
                selectedLaboratory = gestionMantenimientoBinding.spinner.selectedItem.toString()
                selectedDateF = gestionMantenimientoBinding.etFechaFin.text.toString()

                applyFilters(selectedLaboratory, formattedDate, serviceTag, selectedDateF)
            }, year, month, day)

        datePickerDialog.show()
    }

    fun applyFilters( selectedLaboratory: String,
                      selectedDate: String,
                      serviceTag: String,
                      selectedDateF: String) {
        filteredList = mantf.filter { mantenimiento ->
            val labMatches =
                selectedLaboratory == "Seleccionar" || mantenimiento.labname == selectedLaboratory
            val dateMatches = (selectedDate == "yyyy-MM-dd" || mantenimiento.dia >= selectedDate) &&
                    (selectedDateF == "yyyy-MM-dd" || mantenimiento.dia <= selectedDateF)
            val serviceTagMatches = serviceTag.isEmpty() || mantenimiento.computadora.uppercase()
                .contains(serviceTag.uppercase())
            labMatches && dateMatches && serviceTagMatches
        }
        adapter?.updateRecycler(filteredList)
    }

    private fun showDatePickerDialog2() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), R.style.MyDatePickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                var selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                gestionMantenimientoBinding.etFechaFin.setText(formattedDate)
                selectedDateS = gestionMantenimientoBinding.etFechaIn.text.toString()
                serviceTag = gestionMantenimientoBinding.etServiceTag.text.toString()
                selectedLaboratory = gestionMantenimientoBinding.spinner.selectedItem.toString()

                applyFilters(selectedLaboratory, selectedDateS, serviceTag, formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

}