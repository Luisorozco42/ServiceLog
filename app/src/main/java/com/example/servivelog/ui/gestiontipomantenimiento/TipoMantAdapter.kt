package com.example.servivelog.ui.gestiontipomantenimiento

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
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.ui.gestionmantenimiento.view.FragmentGestionMantenimientoDirections
import com.example.servivelog.ui.gestiontipomantenimiento.view.FragmentGestionTipoMantDirections
import com.example.servivelog.ui.gestiontipomantenimiento.viewmodel.GestionTipoMantViewModel

class TipoMantAdapter(
    var context: Context,
    var listTM: List<TipoMantItem>,
    var view: View,
    var gestionTipoMantViewModel: GestionTipoMantViewModel
) : RecyclerView.Adapter<TipoMantAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tipoMan: TextView
        var edit: ImageView
        var delete: ImageView

        init {
            tipoMan = itemView.findViewById(R.id.txtTipoMant)
            edit = itemView.findViewById(R.id.ivEdit)
            delete = itemView.findViewById(R.id.ivDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.tipo_mant_item, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int = listTM.size

    interface OnDeleteClickListener {
        fun onDeleteClicked(tipoMantItem: TipoMantItem)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val tipoMant = listTM[position]
        holder.tipoMan.text = tipoMant.nombre

        holder.edit.setOnClickListener {
            if (tipoMant.nombre == "Sin datos") {
                Toast.makeText(context, "La base de datos se encuentra vacia", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val action =
                    FragmentGestionTipoMantDirections.actionFragmentGestionTipoMantToFragmentEditTipoMant(
                        tipoMant
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }

        holder.delete.setOnClickListener {

            if (tipoMant.nombre != "Sin datos") {
                gestionTipoMantViewModel.deleteTipoMant(tipoMant)
                onDeleteClickListener?.onDeleteClicked(tipoMant)
            } else
                Toast.makeText(context, "No hay datos que eliminar", Toast.LENGTH_SHORT).show()

        }
    }

    fun updateRecycler(listTM: List<TipoMantItem>) {
        this.listTM = listTM
        notifyDataSetChanged()
    }

}