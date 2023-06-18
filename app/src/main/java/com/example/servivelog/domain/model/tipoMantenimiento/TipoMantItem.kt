package com.example.servivelog.domain.model.tipoMantenimiento

import android.os.Parcel
import android.os.Parcelable
import com.example.servivelog.data.database.entities.TipodMantenimientoEntity

data class TipoMantItem(
    var idTM: Int,
    var nombre: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idTM)
        parcel.writeString(nombre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TipoMantItem> {
        override fun createFromParcel(parcel: Parcel): TipoMantItem {
            return TipoMantItem(parcel)
        }

        override fun newArray(size: Int): Array<TipoMantItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun TipodMantenimientoEntity.toDomain() = TipoMantItem(idTM = idTM, nombre = nombre)