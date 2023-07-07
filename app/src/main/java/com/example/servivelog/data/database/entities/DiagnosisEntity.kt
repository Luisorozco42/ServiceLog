package com.example.servivelog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis

@Entity("tblDiagnosis")
data class DiagnosisEntity(

    @PrimaryKey(autoGenerate = true) @ColumnInfo("idDiag") var idD: Int = 0,
    @ColumnInfo("lab")
    var nombrelab: String,
    @ColumnInfo("comp")// ServiceTag de la computadora
    var ServiceTag: String,
    @ColumnInfo("descripcion")
    var descripcion: String,
    @ColumnInfo("Imagen1")
    var ruta1: String,
    @ColumnInfo("Imagen2")
    var ruta2: String,
    @ColumnInfo("Imagen3")
    var ruta3: String,
    @ColumnInfo("Imagen4")
    var ruta4: String,
    @ColumnInfo("Fecha")
    var fecha: String

)

fun DiagnosisItem.toDataBase() =
    DiagnosisEntity(idD, nombrelab, ServiceTag, descripcion, ruta1, ruta2, ruta3, ruta4, fecha)

fun InsertDiagnosis.toInsertDataBase() =
    DiagnosisEntity(
        nombrelab = nombrelab,
        ServiceTag = ServiceTag,
        descripcion = descripcion,
        ruta1 = ruta1,
        ruta2 = ruta2,
        ruta3 = ruta3,
        ruta4 = ruta4,
        fecha = fecha
    )