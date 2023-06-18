package com.example.servivelog.domain.tipoMantenimientoUseCase

import com.example.servivelog.data.TipoMantenimientoRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.tipoMantenimiento.InsertTipoMant
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import javax.inject.Inject

class CUD @Inject constructor(
    private val repository: TipoMantenimientoRepository
){

    suspend fun insertTipoMant(insertTipoMant: InsertTipoMant){
        repository.insertTipoMant(insertTipoMant.toInsertDatabase())
    }

    suspend fun updateTipoMant(tipoMantItem: TipoMantItem){
        repository.updateTipoMant(tipoMantItem.toDatabase())
    }

    suspend fun deleteTipoMant(tipoMantItem: TipoMantItem){
        repository.deleteTipoMant(tipoMantItem.toDatabase())
    }
}