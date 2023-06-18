package com.example.servivelog.ui.gestionlaboratorio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.ui.gestionlaboratorio.view.FragmentLaboratorios
import com.example.servivelog.ui.gestionlaboratorio.view.FragmentLaboratoriosDirections
import com.example.servivelog.ui.gestionlaboratorio.viewmodel.GestionLabViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LabAdapter(
    var context: Context,
    var listL: List<LabItem>,
    var view: View,
    private var gestionLabViewModel: GestionLabViewModel
) : RecyclerView.Adapter<LabAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labName: TextView
        var labDescription: TextView
        var edit: ImageView
        var delete: ImageView
        var logo: ImageView

        init {
            labName = itemView.findViewById(R.id.txtLab)
            labDescription = itemView.findViewById(R.id.tvTexto)
            edit = itemView.findViewById(R.id.ivEdit)
            delete = itemView.findViewById(R.id.ivDelete)
            logo = itemView.findViewById(R.id.iconoCvComputadora)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.laboratory_item, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listL.size
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(labItem: LabItem)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val labList: LabItem = listL[position]
        holder.labName.text =  labList.nombre
        holder.labDescription.text = labList.descripcion

        holder.edit.setOnClickListener{
            if(labList.nombre == "Sin Datos")
                Toast.makeText(context,"No se encontraron Laboratorios en la Base de Datos", Toast.LENGTH_SHORT).show()
            else{
                val action = FragmentLaboratoriosDirections.actionFragmentlaboratoriosToFragmentEditarLaboratorio2(labList)
                Navigation.findNavController(view).navigate(action)
            }
        }
        holder.delete.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val comps = gestionLabViewModel.getAllComps()
                val filtrated = comps.filter { it.ubicacion == labList.nombre }
                if (labList.nombre != "Sin Datos") {
                    if (filtrated.isEmpty()){
                        gestionLabViewModel.deleteLab(labList)
                        onDeleteClickListener?.onDeleteClicked(labList)
                    }else{
                        Toast.makeText(context, "Elimine primeramente todas las computadoras", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "No se encontraron Laboratorios en la Base de Datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        holder.logo.setOnClickListener{
            if(labList.nombre == "Sin Datos")
                Toast.makeText(context,"No se encontraron Laboratorios en la Base de Datos", Toast.LENGTH_SHORT).show()
            else{
                val action = FragmentLaboratoriosDirections.actionFragmentlaboratoriosToGestionarComputadora(labList)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    fun updateRecycler(listL: List<LabItem>){
        this.listL = listL
        notifyDataSetChanged()
    }
}