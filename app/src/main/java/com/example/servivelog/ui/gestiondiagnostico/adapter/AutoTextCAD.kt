package com.example.servivelog.ui.gestiondiagnostico.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AutoTextCAD (context: Context, resource: Int, items: List<String>): ArrayAdapter<String>(context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = getItem(position)
        return view
    }
}