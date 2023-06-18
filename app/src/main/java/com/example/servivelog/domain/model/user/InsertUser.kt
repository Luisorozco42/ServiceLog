package com.example.servivelog.domain.model.user

data class InsertUser(
    var nombre: String,
    var apellido: String,
    var correo: String,
    var clave: String,
    var userName: String
)
