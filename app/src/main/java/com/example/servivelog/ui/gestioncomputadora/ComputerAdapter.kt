package com.example.servivelog.ui.gestioncomputadora

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
import com.example.servivelog.ui.gestioncomputadora.view.GestionarComputadoraDirections
import com.example.servivelog.ui.gestioncomputadora.viewmodel.GestionCompViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComputerAdapter(
    var context: Context,
    var listC: List<ComputerItem>,
    var view: View,
    var gestionCompViewModel: GestionCompViewModel
) : RecyclerView.Adapter<ComputerAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serviceTag: TextView
        var modelo: TextView
        var marca: TextView
        var edit: ImageView
        var delete: ImageView

        init {

            serviceTag = itemView.findViewById(R.id.txtServiceTag)
            modelo = itemView.findViewById(R.id.txtModelo)
            marca = itemView.findViewById(R.id.txtMarca)
            edit = itemView.findViewById(R.id.ivEdit)
            delete = itemView.findViewById(R.id.ivDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.computer_item, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listC.size
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(compList: ComputerItem)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val compList: ComputerItem = listC[position]
        holder.serviceTag.text = compList.serviceTag
        holder.modelo.text = compList.modelo
        holder.marca.text = compList.marca

        holder.edit.setOnClickListener {

            if (compList.serviceTag == "sin datos")
                Toast.makeText(context, "no se encontaron computadoras", Toast.LENGTH_SHORT).show()
            else {
                val action =
                    GestionarComputadoraDirections.actionGestionarComputadoraToFragmentEditarComputadora(
                        compList
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }
        holder.delete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val diag = gestionCompViewModel.getAllDiagnosis()
                val diagFiltered = diag.filter { it.ServiceTag == compList.serviceTag }
                val mant = gestionCompViewModel.getAllMantenimientos()
                val mantFiltered = mant.filter { it.computadora == compList.serviceTag }
                if (compList.serviceTag != "sin datos"){
                    if (diagFiltered.isEmpty() && mantFiltered.isEmpty()){
                        gestionCompViewModel.deleteComputer(compList)
                        onDeleteClickListener?.onDeleteClicked(compList)
                    }else
                        Toast.makeText(context, "Elimine primero todos los mantenimienots y diagnósticos", Toast.LENGTH_SHORT).show()
                }else
                    Toast.makeText(context, "no se encontaron computadoras", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun updateRecycler(listC: List<ComputerItem>){
        this.listC = listC
        notifyDataSetChanged()
    }
}