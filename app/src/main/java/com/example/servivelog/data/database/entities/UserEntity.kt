package com.example.servivelog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.servivelog.domain.model.user.InsertUser
import com.example.servivelog.domain.model.user.UserItem

@Entity("tblUser")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("idU") var idU: Int = 0,
    @ColumnInfo("userName")
    var userName: String,
    @ColumnInfo("nombre")
    var nombre: String,
    @ColumnInfo("apellido")
    var apellido: String,
    @ColumnInfo("email")
    var correo: String,
    @ColumnInfo("clave")
    var clave: String
)

fun InsertUser.toInsertDatabase() =
    UserEntity(
        nombre = nombre,
        apellido = apellido,
        correo = correo,
        clave = clave,
        userName = userName
    )

fun UserItem.toDatabase() =
    UserEntity(
        idU = idU,
        nombre = nombre,
        apellido = apellido,
        correo = correo,
        clave = clave,
        userName = userNamae
    )