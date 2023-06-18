package com.example.servivelog.domain.model.lab

import android.os.Parcel
import android.os.Parcelable
import com.example.servivelog.data.database.entities.LabEntity


data class LabItem(
    var idL: Int,
    var nombre: String,
    var descripcion: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idL)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabItem> {
        override fun createFromParcel(parcel: Parcel): LabItem {
            return LabItem(parcel)
        }

        override fun newArray(size: Int): Array<LabItem?> {
            return arrayOfNulls(size)
        }
    }
}


fun LabEntity.toDomain() = LabItem(idL = idL, nombre = nombre, descripcion = descripcion)
