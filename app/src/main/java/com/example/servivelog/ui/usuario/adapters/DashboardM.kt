package com.example.servivelog.ui.usuario.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem


class DashboardM(
    var context: Context,
    var list: List<MantenimientoCUDItem>,
    var view: View
) : RecyclerView.Adapter<DashboardM.MyHolder>() {
    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var serviceT: TextView
        var lab: TextView
        var fecha: TextView

        init {
            serviceT = itemView.findViewById(R.id.txtServiceTagM)
            lab = itemView.findViewById(R.id.txtLabM)
            fecha = itemView.findViewById(R.id.txtFechaM)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder { //manda a llamar los cards sin linkear a la raiz
        val itemView = LayoutInflater.from(context).inflate(R.layout.dashboard_mitem, parent, false)//hace que la clase my holderempieza a enviar el itemview
        return MyHolder(itemView)
    }

    override fun getItemCount() = list.size //obtiene cantidad obj en el recycler


    override fun onBindViewHolder(holder: MyHolder, position: Int) { //obtiene la posicion

        val mant = list[position]
        holder.serviceT.text = mant.computadora
        holder.lab.text = mant.labname
        holder.fecha.text = mant.dia
    }
}