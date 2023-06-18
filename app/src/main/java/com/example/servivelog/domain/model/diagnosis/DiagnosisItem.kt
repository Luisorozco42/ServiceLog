package com.example.servivelog.domain.model.diagnosis

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.servivelog.data.database.entities.DiagnosisEntity

data class DiagnosisItem(
    var idD: Int,
    var nombrelab: String,
    var ServiceTag: String,
    var descripcion: String,
    var ruta1: String,
    var ruta2: String,
    var ruta3: String,
    var ruta4: String,
    var fecha: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idD)
        parcel.writeString(nombrelab)
        parcel.writeString(ServiceTag)
        parcel.writeString(descripcion)
        parcel.writeString(ruta1)
        parcel.writeString(ruta2)
        parcel.writeString(ruta3)
        parcel.writeString(ruta4)
        parcel.writeString(fecha)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiagnosisItem> {
        override fun createFromParcel(parcel: Parcel): DiagnosisItem {
            return DiagnosisItem(parcel)
        }

        override fun newArray(size: Int): Array<DiagnosisItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun DiagnosisEntity.toDomain() = DiagnosisItem(
    idD = idD,
    nombrelab = nombrelab,
    ServiceTag = ServiceTag,
    descripcion = descripcion,
    ruta1 = ruta1,
    ruta2 = ruta2,
    ruta3 = ruta3,
    ruta4 = ruta4,
    fecha = fecha
)
