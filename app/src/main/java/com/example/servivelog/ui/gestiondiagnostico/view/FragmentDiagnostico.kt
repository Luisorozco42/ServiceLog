package com.example.servivelog.ui.gestiondiagnostico.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentDiagnosticoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.gestiondiagnostico.adapter.DiagnosisAdapter
import com.example.servivelog.ui.gestiondiagnostico.viewmodel.GestioDiagnosisViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class FragmentDiagnostico : Fragment(), DiagnosisAdapter.OnDeleteClickListener {
    private lateinit var fragmentDiagnostico: FragmentDiagnosticoBinding
    private val gestionDiagnosisViewModel: GestioDiagnosisViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private var adapter: DiagnosisAdapter? = null
    private var diagF: List<DiagnosisItem> = emptyList()
    private var filteredList: List<DiagnosisItem> = emptyList()
    private var selectedLaboratory: String = "Seleccionar"
    private var selectedDateS: String = ""
    private var serviceTag: String = ""
    private var selectedDateF: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentDiagnostico = FragmentDiagnosticoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        gestionDiagnosisViewModel.onCreate()
        gestionDiagnosisViewModel.modeloDiagnosis.observe(viewLifecycleOwner) { diagnosis ->

            diagF = diagnosis

            CoroutineScope(Dispatchers.Main).launch {
                val listaC = gestionDiagnosisViewModel.getAllComputer()
                setAdapter(diagF, listaC)
                fragmentDiagnostico.etFechaDIn.setText("yyyy-MM-dd")
                fragmentDiagnostico.etFechaDFin.setText("yyyy-MM-dd")
            }

            fragmentDiagnostico.etServiceTagD.addTextChangedListener { filter ->

                selectedLaboratory = fragmentDiagnostico.spinnerD.selectedItem.toString()
                selectedDateS = fragmentDiagnostico.etFechaDIn.text.toString()
                selectedDateF = fragmentDiagnostico.etFechaDFin.text.toString()

                applyFilters(selectedLaboratory, selectedDateS, filter.toString(), selectedDateF)
            }
        }

        val btnAgregar = fragmentDiagnostico.fbtnagregar

        fragmentDiagnostico.etFechaDIn.setOnClickListener { showDatePickerDialog() }

        fragmentDiagnostico.etFechaDFin.setOnClickListener { showDatePickerDialog2() }

        btnAgregar.setOnClickListener {
            val navController = Navigation.findNavController(requireView())
            navController.navigate(R.id.action_fragmentDiagnostico_to_fragmentAgregarDiagnostico)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val labs = gestionDiagnosisViewModel.getAllLaboratories()
            val listaNombres: ArrayList<String> = ArrayList()

            listaNombres.add("Seleccionar")
            for (l in labs) {
                val nombre = l.nombre
                listaNombres.add(nombre)
            }

            setAdapterSpinner(listaNombres)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Mostrar el cuadro de diálogo de confirmación aquí
            showExitConfirmationDialog()
        }
        callback.isEnabled = true

        fragmentDiagnostico.btnClean.setOnClickListener{
            fragmentDiagnostico.etFechaDFin.setText("yyyy-MM-dd")
            fragmentDiagnostico.etFechaDIn.setText("yyyy-MM-dd")
            fragmentDiagnostico.etServiceTagD.setText("")
            fragmentDiagnostico.spinnerD.setSelection(0)
        }

        return fragmentDiagnostico.root
    }

    private fun setAdapterSpinner(listaNombres: ArrayList<String>) {
        val spinner = fragmentDiagnostico.spinnerD
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

                serviceTag = fragmentDiagnostico.etServiceTagD.text.toString()
                selectedDateS = fragmentDiagnostico.etFechaDIn.text.toString()
                selectedDateF = fragmentDiagnostico.etFechaDFin.text.toString()

                applyFilters(selectedItem, selectedDateS, serviceTag, selectedDateF)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Lógica cuando no se selecciona ningún elemento
            }
        }
    }

    override fun onDeleteCliked(diagnosisItem: DiagnosisItem) {
        val savingData: MutableList<DiagnosisItem> = diagF.toMutableList() //Se guarda mantf para no perder la lista original
        savingData.remove(diagnosisItem)//Se elimina el dato del recycler
        val updateList = adapter?.listD?.toMutableList()
        updateList?.remove(diagnosisItem)
        diagF = updateList!!
        filteredList = filteredList.filter { it != diagnosisItem } //se filtra la lista
        adapter?.updateRecycler(filteredList)// se muestra
        diagF = savingData.toList()//lista recuperada
    }

    private fun setAdapter(diagF: List<DiagnosisItem>, listaC: List<ComputerItem>) {
            adapter = DiagnosisAdapter(
            requireActivity(),
            requireActivity(),
            diagF,
            fragmentDiagnostico.root,
            gestionDiagnosisViewModel,
            listaC
        )
        adapter?.setOnDeleteClickListener(this)
        recyclerView = fragmentDiagnostico.rvDiagnostico
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
            val navController = Navigation.findNavController(fragmentDiagnostico.root)
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
                fragmentDiagnostico.etFechaDIn.setText(formattedDate)
                serviceTag = fragmentDiagnostico.etServiceTagD.text.toString()
                selectedLaboratory = fragmentDiagnostico.spinnerD.selectedItem.toString()
                selectedDateF = fragmentDiagnostico.etFechaDFin.text.toString()
                applyFilters(selectedLaboratory, formattedDate, serviceTag, selectedDateF)
            }, year, month, day)

        datePickerDialog.show()
    }

    fun applyFilters(
        selectedLaboratory: String,
        selectedDate: String,
        serviceTag: String,
        selectedDateF: String
    ) {
        filteredList = diagF.filter { diagnostico ->
            val labMatches =
                selectedLaboratory == "Seleccionar" || diagnostico.nombrelab == selectedLaboratory
            val dateMatches = (selectedDate == "yyyy-MM-dd" || diagnostico.fecha >= selectedDate) &&
                    (selectedDateF == "yyyy-MM-dd" || diagnostico.fecha <= selectedDateF)
            val serviceTagMatches = serviceTag.isEmpty() || diagnostico.ServiceTag.uppercase()
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
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                fragmentDiagnostico.etFechaDFin.setText(formattedDate)
                serviceTag = fragmentDiagnostico.etServiceTagD.text.toString()
                selectedLaboratory = fragmentDiagnostico.spinnerD.selectedItem.toString()
                selectedDateS = fragmentDiagnostico.etFechaDIn.text.toString()

                applyFilters(selectedLaboratory, selectedDateS, serviceTag, formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

}
