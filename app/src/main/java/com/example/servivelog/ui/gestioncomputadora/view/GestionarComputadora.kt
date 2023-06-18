package com.example.servivelog.ui.gestioncomputadora.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.databinding.FragmentGestionarComputadoraBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.ui.gestioncomputadora.ComputerAdapter
import com.example.servivelog.ui.gestioncomputadora.viewmodel.GestionCompViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GestionarComputadora : Fragment(), ComputerAdapter.OnDeleteClickListener{

    private lateinit var gestionarComputadoraBinding: FragmentGestionarComputadoraBinding
    private val gestionCompViewModel: GestionCompViewModel by viewModels()
    private lateinit var addBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val args: GestionarComputadoraArgs by navArgs()
    private lateinit var adapter: ComputerAdapter
    private var compf: List<ComputerItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        gestionarComputadoraBinding = FragmentGestionarComputadoraBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gestionCompViewModel.onCreate()
        gestionCompViewModel.modeloComputer.observe(viewLifecycleOwner) { comp ->
            compf = comp.filter { it.ubicacion == args.laboratorio.nombre || it.ubicacion == "" }
            setAdapter(compf)
            gestionarComputadoraBinding.etServiceTag.addTextChangedListener { filter ->
                val computerChanges = compf.filter {
                    it.serviceTag.uppercase().contains(filter.toString().uppercase())
                }
                adapter.updateRecycler(computerChanges)
            }

        }
        //it es para mostrar corrutinas , clicklistener , all lo que retornes
        addBtn = gestionarComputadoraBinding.fbtnagregar
        addBtn.setOnClickListener {
            val action =
                GestionarComputadoraDirections.actionGestionarComputadoraToFragmentAgregarComputadora(
                    args.laboratorio.nombre
                )
            Navigation.findNavController(it).navigate(action)

        }
        return gestionarComputadoraBinding.root
    }

    override fun onDeleteClicked(compList: ComputerItem) {
        val updatedList = adapter.listC.toMutableList()
        updatedList.remove(compList)
        compf = updatedList
        adapter.updateRecycler(updatedList)
    }

    private fun setAdapter(it: List<ComputerItem>) {
        adapter = ComputerAdapter(
            requireActivity(),
            it,
            gestionarComputadoraBinding.root,
            gestionCompViewModel
        )
        adapter.setOnDeleteClickListener(this)
        recyclerView = gestionarComputadoraBinding.rvComputadora
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }
}