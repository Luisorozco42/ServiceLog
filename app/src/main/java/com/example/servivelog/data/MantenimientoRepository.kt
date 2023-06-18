package com.example.servivelog.data

import androidx.annotation.WorkerThread
import com.example.servivelog.data.database.dao.MantenimientoDao
import com.example.servivelog.data.database.entities.MantenimientoEntity
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.toDomainCUD
import javax.inject.Inject

class MantenimientoRepository @Inject constructor(
    private val mantenimientoDao: MantenimientoDao
) {

    suspend fun getAllMantenimientos(): List<MantenimientoCUDItem>{
        val response: List<MantenimientoEntity> = mantenimientoDao.getAllMaintenances()
        return response.map { it.toDomainCUD() }
    }
    //declaramos la fun en el repositorio de mantenimiento
    suspend fun getLastfourMaintenances(): List<MantenimientoCUDItem> {
        val response: List<MantenimientoEntity> = mantenimientoDao.getLastfourMaintenances()
        return response.map { it.toDomainCUD() }
    }

    @WorkerThread
    suspend fun insertMantenimiento(mantenimientoEntity: MantenimientoEntity){
        mantenimientoDao.insertMaintenance(mantenimientoEntity)
    }

    @WorkerThread
    suspend fun updateMantenimiento(mantenimientoEntity: MantenimientoEntity){
        mantenimientoDao.updateMaintenance(mantenimientoEntity)
    }

    @WorkerThread
    suspend fun deleteMantenimiento(mantenimientoEntity: MantenimientoEntity){
        mantenimientoDao.deleteMaintenance(mantenimientoEntity)
    }
}