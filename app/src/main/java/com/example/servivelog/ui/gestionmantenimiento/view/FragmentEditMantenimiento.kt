package com.example.servivelog.ui.gestionmantenimiento.view

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.servivelog.databinding.FragmentEditMantenimientoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.ui.gestionmantenimiento.adapter.AutoTextLabAdapter
import com.example.servivelog.ui.gestionmantenimiento.adapter.AutotextComptAdapter
import com.example.servivelog.ui.gestionmantenimiento.viewmodel.GestionManteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentEditMantenimiento : Fragment() {

    private lateinit var editMantenimientoBinding: FragmentEditMantenimientoBinding
    private lateinit var mantenimientoCUDItem: MantenimientoCUDItem
    private val args: FragmentEditMantenimientoArgs by navArgs()
    private val gestionManteViewModel: GestionManteViewModel by viewModels()
    private lateinit var linearLayout: LinearLayoutCompat
    private lateinit var listTM: List<TipoMantItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        editMantenimientoBinding = FragmentEditMantenimientoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        ubicandoCheckbox()
        CoroutineScope(Dispatchers.Main).launch {
            val lab = gestionManteViewModel.getAllLabs()
            val comp = gestionManteViewModel.getAllComputers()
            obteniendoDatos(comp)
            setLabAdapter(lab)
        }
        val btneditar = editMantenimientoBinding.btnEditar

        editMantenimientoBinding.tvAddMant.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(com.example.servivelog.R.id.action_fragmentEditMantenimiento_to_fragmentGestionTipoMant)
        }

        btneditar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val listL = gestionManteViewModel.getAllLabs()
                val listC = gestionManteViewModel.getAllComputers()
                editarDatos(it, listL, listC)
            }
        }

        return editMantenimientoBinding.root
    }

    private fun editarDatos(it: View, listL: List<LabItem>, listC: List<ComputerItem>) {

        val lab = listL.find { it.nombre == editMantenimientoBinding.ctvLab.text.toString() }
        val comp =
            listC.find { it.ubicacion == editMantenimientoBinding.ctvLab.text.toString() && it.serviceTag == editMantenimientoBinding.ctvServiceTag.text.toString() }
        val tipoMant = confirmarCheckBox()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // formato deseado
        val date = Date() // obtiene la fecha actual
        val formattedDate = dateFormat.format(date)

        if (lab == null || comp == null || tipoMant == "" || editMantenimientoBinding.etDescripcion.text.toString()
                .isEmpty()
        )
            Toast.makeText(
                requireContext(),
                "No se puede editar el registro porque faltan datos, el laboratorio no existe o la computadora no existe",
                Toast.LENGTH_SHORT
            ).show()
        else {
            mantenimientoCUDItem.labname = lab.nombre
            mantenimientoCUDItem.computadora = comp.serviceTag
            mantenimientoCUDItem.tipoLimpieza = tipoMant
            mantenimientoCUDItem.desc = editMantenimientoBinding.etDescripcion.text.toString()
            mantenimientoCUDItem.dia = formattedDate

            gestionManteViewModel.updateMantenimiento(mantenimientoCUDItem)
            val navController = Navigation.findNavController(it)
            navController.popBackStack()
        }

    }

    private fun obteniendoDatos(comp: List<ComputerItem>) {
        mantenimientoCUDItem = args.mantenimiento

        val cb = mantenimientoCUDItem.tipoLimpieza.split(". ")

        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)

            // Verifica si el elemento es un CheckBox
            if (view is CheckBox) {
                for (j in 0 until cb.size){
                    if (view.text.toString() == cb[j]){
                        view.isChecked = true
                    }
                }
            }
        }

        editMantenimientoBinding.ctvLab.setText(mantenimientoCUDItem.labname)
        editMantenimientoBinding.ctvServiceTag.setText(mantenimientoCUDItem.computadora)
        val com = comp.filter { it.ubicacion == mantenimientoCUDItem.labname }
        setCompAdapter(com)
        editMantenimientoBinding.etDescripcion.setText(mantenimientoCUDItem.desc)
    }

    private fun ubicandoCheckbox() {
        CoroutineScope(Dispatchers.Main).launch {
            listTM = gestionManteViewModel.tiposDeMant()
            linearLayout = editMantenimientoBinding.almacenTipoMant
            linearLayout.removeAllViews()
            if(!listTM.isEmpty()){
                for (tm in listTM){
                    val checkBox = CheckBox(requireContext())
                    checkBox.text = tm.nombre
                    linearLayout.addView(checkBox)
                }
                editMantenimientoBinding.tvTipos.isVisible = false
            }else{
                editMantenimientoBinding.tvTipos.isVisible = true
            }
        }

    }

    private fun confirmarCheckBox(): String {

        var result = ""
        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)

            // Verifica si el elemento es un CheckBox
            if (view is CheckBox) {
                // Comprueba si el CheckBox está seleccionado
                val isChecked = view.isChecked
                if (isChecked) {
                    result += view.text.toString() + ". "
                }
            }
        }
        return result
    }

    private fun setLabAdapter(lab: List<LabItem>) {
        val adapter = AutoTextLabAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            lab.map { it.nombre })
        editMantenimientoBinding.ctvLab.setAdapter(adapter)

        editMantenimientoBinding.ctvLab.setOnItemClickListener { _, _, position, _ ->
            val selectedLab = lab[position]
            CoroutineScope(Dispatchers.Main).launch {
                val computerList = gestionManteViewModel.getAllComputers()
                    .filter { it.ubicacion == selectedLab.nombre }
                setCompAdapter(computerList)
            }
        }
    }

    private fun setCompAdapter(comp: List<ComputerItem>) {
        val adapter = AutotextComptAdapter(
            requireActivity(),
            R.layout.simple_dropdown_item_1line,
            comp.map { it.serviceTag })
        editMantenimientoBinding.ctvServiceTag.setAdapter(adapter)
    }
}