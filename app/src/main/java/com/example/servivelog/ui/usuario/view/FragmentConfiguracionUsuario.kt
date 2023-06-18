package com.example.servivelog.ui.usuario.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentConfiguracionUsuarioBinding
import com.example.servivelog.domain.model.user.ActiveUser
import com.example.servivelog.domain.model.user.UserItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.usuario.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentConfiguracionUsuario : Fragment() {
    private lateinit var configuracionUsaurioBinding: FragmentConfiguracionUsuarioBinding
    private val userVielModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuracionUsaurioBinding = FragmentConfiguracionUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        obteniendoDatos()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Mostrar el cuadro de diálogo de confirmación aquí
            showExitConfirmationDialog()
        }
        callback.isEnabled = true
        val btnEditar = configuracionUsaurioBinding.btnEditar
        val btnEliminar = configuracionUsaurioBinding.btnEliminar

        btnEditar.setOnClickListener {
            guardarCambios()
        }
        btnEliminar.setOnClickListener {
            confirmationDelete()
        }

        return configuracionUsaurioBinding.root
    }

    private fun eliminarUsuario() {
        val userItem =
            UserItem(
                idU = ActiveUser.idU,
                userNamae = ActiveUser.userName,
                nombre = ActiveUser.nombre,
                apellido = ActiveUser.apellido,
                correo =  ActiveUser.correo,
                clave = ActiveUser.clave
            )

        userVielModel.deelteUser(userItem)
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
            false //esto funciona para dejar de mostrar el navigation view despues de cerrar sesion
        val navController = Navigation.findNavController(configuracionUsaurioBinding.root)
        navController.popBackStack()

    }

    private fun guardarCambios() {
        val contrasena = configuracionUsaurioBinding.etContraseA.text.toString()
        val confirmar = configuracionUsaurioBinding.etConfirmacion.text.toString()

        if (contrasena != ActiveUser.clave) {
            if (contrasena == confirmar) {
                ActiveUser.userName = configuracionUsaurioBinding.etUser.text.toString()
                ActiveUser.nombre = configuracionUsaurioBinding.etNombre.text.toString()
                ActiveUser.apellido = configuracionUsaurioBinding.etApellido.text.toString()
                ActiveUser.correo = configuracionUsaurioBinding.etCorreo.text.toString()
                ActiveUser.clave = configuracionUsaurioBinding.etContraseA.text.toString()
                val userItem =
                    UserItem(
                        idU = ActiveUser.idU,
                        userNamae = ActiveUser.userName,
                        nombre = ActiveUser.nombre,
                        apellido = ActiveUser.apellido,
                        correo =  ActiveUser.correo,
                        clave = ActiveUser.clave
                    )
                userVielModel.updateUser(userItem)
                Toast.makeText(
                    requireContext(),
                    "La contraseña se ha guardado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                configuracionUsaurioBinding.etContraseA.setText("")
                configuracionUsaurioBinding.etConfirmacion.setText("")
            } else {
                Toast.makeText(
                    requireContext(),
                    "La contraseña no son iguales",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
            Toast.makeText(
                requireContext(),
                "La contraseña  es la misma que la anterior",
                Toast.LENGTH_SHORT
            ).show()
        }



        ActiveUser.userName = configuracionUsaurioBinding.etUser.text.toString()
        ActiveUser.nombre = configuracionUsaurioBinding.etNombre.text.toString()
        ActiveUser.apellido = configuracionUsaurioBinding.etApellido.text.toString()
        ActiveUser.correo = configuracionUsaurioBinding.etCorreo.text.toString()
        ActiveUser.clave = configuracionUsaurioBinding.etContraseA.text.toString()

    }

    fun obteniendoDatos() {
        val userItem =
            UserItem(
                ActiveUser.idU,
                ActiveUser.userName,
                ActiveUser.nombre,
                ActiveUser.apellido,
                ActiveUser.correo,
                ActiveUser.clave
            )
        configuracionUsaurioBinding.etUser.setText(ActiveUser.userName)
        configuracionUsaurioBinding.etNombre.setText(ActiveUser.nombre)
        configuracionUsaurioBinding.etApellido.setText(ActiveUser.apellido)
        configuracionUsaurioBinding.etCorreo.setText(ActiveUser.correo)
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Terminar la sesión")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
                false //esto funciona para dejar de mostrar el navigation view despues de cerrar sesion
            val navController = Navigation.findNavController(configuracionUsaurioBinding.root)
            navController.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        val dialog = alertDialogBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.GREEN)
    }

    private fun confirmationDelete() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Eliminar usuario y salir de la aplicacion")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas eliminarlo?")
        alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
            eliminarUsuario()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        val dialog = alertDialogBuilder.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.GREEN)
    }

}