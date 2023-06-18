package com.example.servivelog.domain.model.computer

import android.os.Parcel
import android.os.Parcelable
import com.example.servivelog.data.database.entities.ComputerEntity
import com.example.servivelog.data.database.entities.LabEntity

data class ComputerItem (
    var idC: Int,
    var nombre: String,
    var descripcion: String,
    var marca: String,
    var modelo: String,
    var procesador: String,
    var ram: Int,
    var almacenamiento: Int,
    var serviceTag: String,
    var noInventario: String,
    var ubicacion: String
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idC)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeString(marca)
        parcel.writeString(modelo)
        parcel.writeString(procesador)
        parcel.writeInt(ram)
        parcel.writeInt(almacenamiento)
        parcel.writeString(serviceTag)
        parcel.writeString(noInventario)
        parcel.writeString(ubicacion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComputerItem> {
        override fun createFromParcel(parcel: Parcel): ComputerItem {
            return ComputerItem(parcel)
        }

        override fun newArray(size: Int): Array<ComputerItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun ComputerEntity.toDomain() = ComputerItem(idC = idC,
    nombre = nombre,
    descripcion = descripcion,
    marca = marca,
    modelo = modelo,
    procesador = procesador,
    ram = ram,
    almacenamiento = almacenamiento,
    serviceTag = serviceTag,
    noInventario = noInventario,
    ubicacion = ubicacion)