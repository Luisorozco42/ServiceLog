package com.example.servivelog.ui.gestioneasteregg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servivelog.databinding.FragmentAcercaDeBinding

class AcercaDe : Fragment() {

    private lateinit var acercaDeBinding: FragmentAcercaDeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        acercaDeBinding = FragmentAcercaDeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return acercaDeBinding.root
    }
}