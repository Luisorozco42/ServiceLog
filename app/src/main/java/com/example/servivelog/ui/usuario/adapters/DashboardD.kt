package com.example.servivelog.ui.usuario.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.ui.Hilt_MainActivity

class DashboardD(
    var context: Context,
    var list: List<DiagnosisItem>,
    var view: View
): RecyclerView.Adapter<DashboardD.MiHolder>() {

    inner class MiHolder(inteView: View):RecyclerView.ViewHolder(inteView){
            var servicetag: TextView
            var laboratorio: TextView
            var fecha: TextView

            init{
                servicetag = itemView.findViewById(R.id.txtServiceTag)
                laboratorio = itemView.findViewById(R.id.txtLab)
                fecha = itemView.findViewById(R.id.txtFecha)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiHolder {
       val itemView = LayoutInflater.from(context).inflate(R.layout.dashboardditem, parent, false)
        return MiHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MiHolder, position: Int) {
         val diag= list[position]
        holder.servicetag.text = diag.ServiceTag
        holder.laboratorio.text = diag.nombrelab
        holder.fecha.text = diag.fecha
    }
}