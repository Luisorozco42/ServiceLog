package com.example.servivelog.ui.gestionmantenimiento.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentAgregarMantenimientoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
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
class FragmentAgregarMantenimiento : Fragment() {

    private lateinit var agregarMantenimientoBinding: FragmentAgregarMantenimientoBinding
    private val gestionManteViewModel: GestionManteViewModel by viewModels()
    private lateinit var listTM: List<TipoMantItem>
    private lateinit var linearLayout: LinearLayoutCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        agregarMantenimientoBinding = FragmentAgregarMantenimientoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        ubicandoCheckbox()
        val agregarbtn = agregarMantenimientoBinding.btnAgregar

        agregarMantenimientoBinding.tvAddMant.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentAgregarMantenimiento_to_fragmentGestionTipoMant)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val lab = gestionManteViewModel.getAllLabs()
            setLabAdapter(lab)
        }

        agregarbtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{
                val listL = gestionManteViewModel.getAllLabs()
                val listC = gestionManteViewModel.getAllComputers()

                val insertMantenimiento: MantenimientoItem = enviarDatos(listL, listC)

                if (insertMantenimiento.labname == "" || insertMantenimiento.computadora == "")
                    Toast.makeText(requireContext(), "No se puede insertar en la bd porque no existe el lab, la computadora o faltan datos", Toast.LENGTH_SHORT).show()
                else{
                    gestionManteViewModel.insertMantenimiento(insertMantenimiento)
                    val navController = Navigation.findNavController(it)
                    navController.popBackStack()
                }
            }
        }
        return agregarMantenimientoBinding.root
    }

    private fun setLabAdapter(lab: List<LabItem>) {
        val adapter = AutoTextLabAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, lab.map { it.nombre })
        agregarMantenimientoBinding.ctvLab.setAdapter(adapter)

            agregarMantenimientoBinding.ctvLab.setOnItemClickListener { _, _, position, _ ->
            val selectedLab = lab[position]
            CoroutineScope(Dispatchers.Main).launch {
                val computerList = gestionManteViewModel.getAllComputers().filter { it.ubicacion == selectedLab.nombre }
                setCompAdapter(computerList)
            }
        }
    }

    private fun setCompAdapter(comp: List<ComputerItem>) {
        val adapter = AutotextComptAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line ,comp.map { it.serviceTag })
        agregarMantenimientoBinding.ctvServiceTag.setAdapter(adapter)
    }

    private fun enviarDatos(listL: List<LabItem>, listC: List<ComputerItem>): MantenimientoItem {

        val lab = listL.find { it.nombre == agregarMantenimientoBinding.ctvLab.text.toString() }
        val comp = listC.find { it.serviceTag == agregarMantenimientoBinding.ctvServiceTag.text.toString() }
        val desc = agregarMantenimientoBinding.etDescripcion.text.toString()
        val tipoMant = confirmarCheckBox()

        if (lab == null || comp == null || tipoMant == "" || desc.isEmpty()){
            return MantenimientoItem("","","","","")
        }else{


            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) //con este formato vamos a trabajar
            val date = Date() // se obtiene la fecha actual
            val formattedDate = dateFormat.format(date)

            return MantenimientoItem(
                lab.nombre,
                comp.serviceTag,
                tipoMant,
                desc,
                formattedDate
            )
        }

    }

    private fun ubicandoCheckbox() {
        CoroutineScope(Dispatchers.Main).launch{
            listTM = gestionManteViewModel.tiposDeMant()
            linearLayout = agregarMantenimientoBinding.almacenTipoMant
            linearLayout.removeAllViews()
            if(!listTM.isEmpty()){
                for (tm in listTM){
                    val checkBox = CheckBox(requireContext())
                    checkBox.text = tm.nombre
                    linearLayout.addView(checkBox)
                }
                agregarMantenimientoBinding.tvTipos.isVisible = false
            }else{
                agregarMantenimientoBinding.tvTipos.isVisible = true
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
}