package com.example.servivelog.domain.model.user

import android.os.Parcel
import android.os.Parcelable
import com.example.servivelog.data.database.entities.UserEntity

data class UserItem(
    var idU: Int = 0,
    var nombre: String,
    var apellido: String,
    var correo: String,
    var clave: String,
    var userNamae: String
) : Parcelable {
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
        parcel.writeInt(idU)
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(correo)
        parcel.writeString(clave)
        parcel.writeString(userNamae)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserItem> {
        override fun createFromParcel(parcel: Parcel): UserItem {
            return UserItem(parcel)
        }

        override fun newArray(size: Int): Array<UserItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun UserEntity.toDomain() =
    UserItem(
        idU = idU,
        nombre = nombre,
        apellido = apellido,
        correo = correo,
        clave = clave,
        userNamae = userName
    )


