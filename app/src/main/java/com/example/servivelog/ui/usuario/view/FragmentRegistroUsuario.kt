package com.example.servivelog.ui.usuario.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentRegistroUsuarioBinding
import com.example.servivelog.domain.model.user.InsertUser
import com.example.servivelog.ui.usuario.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentRegistroUsuario : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var registroUBinding: FragmentRegistroUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        registroUBinding = FragmentRegistroUsuarioBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registroUBinding.btnRegistrase.setOnClickListener {
            var insertUser: InsertUser
            if (registroUBinding.tiContraseA.text.toString() == registroUBinding.etConfirmarContraseA.text.toString()) {
                insertUser = InsertUser(
                    nombre = registroUBinding.etNombre.text.toString(),
                    apellido = registroUBinding.etApellido.text.toString(),
                    correo = registroUBinding.etCorreo.text.toString(),
                    clave = registroUBinding.etConfirmarContraseA.text.toString(),
                    userName = registroUBinding.etUser.text.toString()
                )
                Toast.makeText(
                    requireContext(),
                    "Los datos se han guardado correctamente.",
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.insertUser(insertUser)
                val navController = Navigation.findNavController(it)
                navController.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Las contraseñas no son iguales.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        return registroUBinding.root
    }


}