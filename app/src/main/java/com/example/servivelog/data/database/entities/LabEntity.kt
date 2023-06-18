package com.example.servivelog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem

@Entity("tblLaboratorio")
data class LabEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("idLab") var idL: Int = 0,
    @ColumnInfo("nombreLab")
    var nombre: String,
    @ColumnInfo("descripcion")
    var descripcion: String
)
fun InsertLab.toInsertDatabase() = LabEntity(nombre = nombre, descripcion = descripcion)
fun LabItem.toDatabase() = LabEntity(idL = idL, nombre = nombre, descripcion = descripcion)