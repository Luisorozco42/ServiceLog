package com.example.servivelog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.servivelog.domain.model.tipoMantenimiento.InsertTipoMant
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem

@Entity("tbl_tipoMant")
data class TipodMantenimientoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("idTM") var idTM: Int = 0,
    @ColumnInfo("nombre")
    var nombre: String
)

fun TipoMantItem.toDatabase() = TipodMantenimientoEntity(idTM = idTM, nombre = nombre)
fun InsertTipoMant.toInsertDatabase() = TipodMantenimientoEntity(nombre = nombre)