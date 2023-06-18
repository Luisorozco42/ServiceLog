package com.example.servivelog.domain.mantenimientousecase

import com.example.servivelog.data.ComputerRepository
import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.MantenimientoRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
import javax.inject.Inject

class CUDMantenimiento @Inject constructor(
    private val mantenimientoRepository: MantenimientoRepository,
    private val computadoraRepository: ComputerRepository,
    private val labRepository: LabRepository
) {
    suspend fun getAllComputers(): List<ComputerItem>{
        return computadoraRepository.getAllComputers()
    }
    suspend fun getAllLaboratories(): List<LabItem>{
        return labRepository.getAllLabs()
    }
    suspend fun insertMantenimiento(mantenimientoItem: MantenimientoItem){
        mantenimientoRepository.insertMantenimiento(mantenimientoItem.toInsertDatabase())//Mapper especializado para insertar
    }

    suspend fun updateMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        mantenimientoRepository.updateMantenimiento(mantenimientoCUDItem.toDatabase())//Mapper especialiozado para actualizar
    }

    suspend fun deleteMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        mantenimientoRepository.deleteMantenimiento(mantenimientoCUDItem.toDatabase())//Mapper especializado para eliminar
    }
     //mandamos a pedir que nos devuelva la lista de last 4
    suspend fun getLastMaintenance():List<MantenimientoCUDItem>{
        return mantenimientoRepository.getLastfourMaintenances()
    }
    //mandamos a pedir todos los mantenimientos
    suspend fun getAllmantenimiento():List<MantenimientoCUDItem>{
        return mantenimientoRepository.getAllMantenimientos()
    }

}