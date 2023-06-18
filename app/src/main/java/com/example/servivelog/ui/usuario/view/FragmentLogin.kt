package com.example.servivelog.ui.usuario.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.Navigation
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentLoginBinding

import com.example.servivelog.domain.model.user.ActiveUser
import com.example.servivelog.domain.model.user.UserItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.usuario.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FragmentLogin : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = FragmentLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        loginBinding.btnIniciar.setOnClickListener {

            runBlocking {
                CoroutineScope(Dispatchers.Main).launch {
                    val validar = userViewModel.getUserByUsername()
                    validarUsuario(validar)
                }
            }
        }

        loginBinding.tvRegistro.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentLogin_to_fragmentRegistroUsuario)
        }

        loginBinding.logo.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentLogin_to_acercaDe)
        }

        return loginBinding.root
    }

    private fun validarUsuario(validar: List<UserItem>) {
        for (v in validar) {
            if (v.userNamae == loginBinding.txtUser.text.toString()) {
                ActiveUser.idU = v.idU
                ActiveUser.userName = v.userNamae
                ActiveUser.nombre = v.nombre
                ActiveUser.apellido = v.apellido
                ActiveUser.clave = v.clave
                ActiveUser.correo = v.correo
            }

        }

        if (ActiveUser.clave == loginBinding.txtPassword.text.toString() && ActiveUser.clave != "" && ActiveUser.userName == loginBinding.txtUser.text.toString() && ActiveUser.clave != "") {
            loginBinding.txtUser.setText("")
            loginBinding.txtPassword.setText("")
            val navController = Navigation.findNavController(loginBinding.root)
            navController.navigate(R.id.action_fragmentLogin_to_fragmentMenu)
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
                true //esto funciona para mostrar el navigation view despues de iniciar sesion
        } else {
            Toast.makeText(context, "Contraseña incorrecta o no ha registrado este usuario", Toast.LENGTH_LONG).show()
        }


    }
}