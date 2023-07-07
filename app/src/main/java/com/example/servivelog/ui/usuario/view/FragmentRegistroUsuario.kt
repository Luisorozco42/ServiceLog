package com.example.servivelog.ui.usuario.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.servivelog.core.ex.onTextChanged
import com.example.servivelog.core.ex.setErrorIfInvalid
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
        val usuarioTil = registroUBinding.tilUsuario
        val usuarioText = registroUBinding.etUser
        val nombreTil = registroUBinding.tilNombre
        val nombreText = registroUBinding.etNombre
        val apellidoTil = registroUBinding.tilApellido
        val apellidoText = registroUBinding.etApellido
        val correoTil = registroUBinding.tilCorreo
        val correoText = registroUBinding.etCorreo
        val contraTil = registroUBinding.tilContra
        val contraText = registroUBinding.etContraseA
        val confirmarTil = registroUBinding.tilConfirmarContra
        val confirmarText = registroUBinding.etConfirmarContraseA

        usuarioText.onTextChanged { usuarioTil.setErrorIfInvalid(it, "La información ingresada no es válida", "El campo está vacío") }
        nombreText.onTextChanged { nombreTil.setErrorIfInvalid(it, "La información ingresada no es válida", "El campo está vacío") }
        apellidoText.onTextChanged { apellidoTil.setErrorIfInvalid(it, "La información ingresada no es válida", "El campo está vacío") }
        correoText.onTextChanged { correoTil.setErrorIfInvalid(it, "La información no es válida", "El campo está vacío") }
        contraText.onTextChanged { contraTil.setErrorIfInvalid(it, "La información no es válida", "El campo está vacío") }
        confirmarText.onTextChanged { confirmarTil.setErrorIfInvalid(it, "La información no es válida", "El campo está vacío") }

        registroUBinding.btnRegistrase.setOnClickListener {
            var insertUser: InsertUser

            usuarioTil.setErrorIfInvalid(usuarioText.text.toString(),"La información ingresada no es válida", "El campo está vacío")
            nombreTil.setErrorIfInvalid(nombreText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            apellidoTil.setErrorIfInvalid(apellidoText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            correoTil.setErrorIfInvalid(correoText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            contraTil.setErrorIfInvalid(contraText.text.toString(), "La información ingresada no es válida", "El campo está vacío")
            confirmarTil.setErrorIfInvalid(confirmarText.text.toString(), "La información ingresada no es válida", "El campo está vacío")


            if (contraText.text.toString() == confirmarText.text.toString()) {
                if(usuarioText.text.toString().trim().isNotEmpty() &&
                    nombreText.text.toString().trim().isNotEmpty() &&
                    apellidoText.text.toString().trim().isNotEmpty() &&
                    correoText.text.toString().trim().isNotEmpty() &&
                        contraText.text.toString().trim().isNotEmpty() &&
                        contraText.toString().trim().isNotEmpty()) {

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
                }else{
                    Toast.makeText(requireContext(), "Hay casillas sin datos", Toast.LENGTH_SHORT).show()
                }
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