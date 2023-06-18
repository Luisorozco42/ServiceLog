package com.example.servivelog.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.servivelog.data.database.dao.TipoMantenimientoDao
import com.example.servivelog.data.database.entities.TipodMantenimientoEntity
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.domain.model.tipoMantenimiento.toDomain
import javax.inject.Inject

class TipoMantenimientoRepository @Inject constructor(
    private val tipoMantenimientoDao: TipoMantenimientoDao
) {

     suspend fun getAllTipoMantenimiento(): List<TipoMantItem> {
        val response: List<TipodMantenimientoEntity> = tipoMantenimientoDao.getAllTipoMant()
        return response.map { it.toDomain() }
    }

    suspend fun insertTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity){
        tipoMantenimientoDao.insertTipoMant(tipodMantenimientoEntity)
    }

    suspend fun updateTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity){
        tipoMantenimientoDao.updateTipoMant(tipodMantenimientoEntity)
    }

    suspend fun deleteTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity){
        tipoMantenimientoDao.deleteTipoMant(tipodMantenimientoEntity)
    }
}