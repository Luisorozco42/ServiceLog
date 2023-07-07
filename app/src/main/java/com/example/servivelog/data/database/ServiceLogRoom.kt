package com.example.servivelog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.servivelog.data.database.dao.ComputerDao
import com.example.servivelog.data.database.dao.DiagnosisDao
import com.example.servivelog.data.database.dao.LabDao
import com.example.servivelog.data.database.dao.MantenimientoDao
import com.example.servivelog.data.database.dao.TipoMantenimientoDao
import com.example.servivelog.data.database.dao.UserDao
import com.example.servivelog.data.database.entities.ComputerEntity
import com.example.servivelog.data.database.entities.DiagnosisEntity
import com.example.servivelog.data.database.entities.LabEntity
import com.example.servivelog.data.database.entities.MantenimientoEntity
import com.example.servivelog.data.database.entities.TipodMantenimientoEntity
import com.example.servivelog.data.database.entities.UserEntity

@Database(
    entities = [ComputerEntity::class,
        DiagnosisEntity::class, LabEntity::class, MantenimientoEntity::class, UserEntity::class, TipodMantenimientoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ServiceLogRoom : RoomDatabase() {
    abstract fun computerDao(): ComputerDao

    abstract fun diagnosisDao(): DiagnosisDao

    abstract fun labDao(): LabDao

    abstract fun mantenimientoDao(): MantenimientoDao

    abstract fun userDao(): UserDao

    abstract fun tipoMantenimientoDao(): TipoMantenimientoDao
}