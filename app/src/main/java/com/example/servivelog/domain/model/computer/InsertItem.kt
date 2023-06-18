package com.example.servivelog.domain.model.computer

import com.example.servivelog.data.database.entities.ComputerEntity

data class InsertItem(
    val nombre: String,
    val descripcion: String,
    val marca: String,
    val modelo: String,
    val procesador: String,
    val ram: Int,
    val almacenamiento: Int,
    val serviceTag: String,
    val noInventario: String,
    val ubicacion: String
)

