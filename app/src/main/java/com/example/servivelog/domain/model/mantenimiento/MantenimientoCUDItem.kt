package com.example.servivelog.domain.model.mantenimiento

import android.os.Parcel
import android.os.Parcelable
import com.example.servivelog.data.database.entities.MantenimientoEntity
import java.sql.Date

data class MantenimientoCUDItem(
    var idM: Int,
    var labname: String,
    var computadora: String,
    var tipoLimpieza: String,
    var desc: String,
    var dia: String
): Parcelable {
    // pequeño tutorial de como agregar el Parcelable
    // primero escriben el ": parcelable" que se encuentra en la linea 12
    // Segundo saldra un error en la linea 7 y le dan alt + enter
    // escogen el add implementations
    // el parcelable funciona para enviar datos entre fragmentos que navegan
    //por ultimo les dara error en todos los que digan readString() que en mi caso inician desde la linea 23
    //lo unico por hacer es añadir esto "!!"
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idM)
        parcel.writeString(labname)
        parcel.writeString(computadora)
        parcel.writeString(tipoLimpieza)
        parcel.writeString(desc)
        parcel.writeString(dia)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MantenimientoCUDItem> {
        override fun createFromParcel(parcel: Parcel): MantenimientoCUDItem {
            return MantenimientoCUDItem(parcel)
        }

        override fun newArray(size: Int): Array<MantenimientoCUDItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun MantenimientoEntity.toDomainCUD() = MantenimientoCUDItem(idM = idM, labname = labname, computadora = computadora, tipoLimpieza = tipoLimpieza, desc = desc, dia = dia.toString())