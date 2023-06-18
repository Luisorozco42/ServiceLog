package com.example.servivelog.domain.model.mantenimiento

import com.example.servivelog.data.database.entities.MantenimientoEntity
import java.sql.Date

data class MantenimientoItem(
    var labname: String,
    var computadora: String,
    var tipoLimpieza: String,
    var desc: String,
    var dia: String
)

fun MantenimientoEntity.toDomain() = MantenimientoItem(labname = labname, computadora = computadora, tipoLimpieza = tipoLimpieza, desc = desc, dia = dia.toString())
