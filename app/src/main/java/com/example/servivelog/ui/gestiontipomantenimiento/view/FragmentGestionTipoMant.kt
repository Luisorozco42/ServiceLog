package com.example.servivelog.ui.gestiontipomantenimiento.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentGestionTipoMantBinding
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.ui.gestiontipomantenimiento.TipoMantAdapter
import com.example.servivelog.ui.gestiontipomantenimiento.viewmodel.GestionTipoMantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentGestionTipoMant : Fragment(), TipoMantAdapter.OnDeleteClickListener {

    private lateinit var binding: FragmentGestionTipoMantBinding
    private val gestionTipoMantViewModel: GestionTipoMantViewModel by viewModels()
    private lateinit var adapter: TipoMantAdapter
    private var tipoMantf: List<TipoMantItem> = emptyList<TipoMantItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentGestionTipoMantBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding.fbtnagregarTipoMant.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentGestionTipoMant_to_fragmentAgregarTipoMant)
        }

        gestionTipoMantViewModel.onCreate()

        gestionTipoMantViewModel.modeloTipoMant.observe(viewLifecycleOwner){
            tipoMantf = it
            setAdapter(tipoMantf)

            binding.etTipoMant.addTextChangedListener { filter->
                val tipoMantFiltrados = tipoMantf.filter { it.nombre.uppercase().contains(filter.toString().uppercase()) }
                adapter.updateRecycler(tipoMantFiltrados)
            }
        }
        return binding.root
    }

    override fun onDeleteClicked(tipoMantItem: TipoMantItem) {
        val updatedList = adapter.listTM.toMutableList()
        updatedList.remove(tipoMantItem)
        tipoMantf = updatedList
        adapter.updateRecycler(updatedList)
    }

    private fun setAdapter(it: List<TipoMantItem>) {
        adapter = TipoMantAdapter(requireContext(), it, binding.root, gestionTipoMantViewModel)
        adapter.setOnDeleteClickListener(this)
        val recyclerView = binding.rvTipoMant
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }

}