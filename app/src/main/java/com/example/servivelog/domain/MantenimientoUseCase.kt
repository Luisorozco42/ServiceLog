package com.example.servivelog.domain

import com.example.servivelog.data.MantenimientoRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MantenimientoUseCase @Inject constructor(
    private  val mantenimientoRepository: MantenimientoRepository
) {
    suspend operator fun invoke(): List<MantenimientoCUDItem>{
        return mantenimientoRepository.getAllMantenimientos()
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

        val mantenimientos: List<MantenimientoCUDItem> = mantenimientoRepository.getLastfourMaintenances()
        var last4MM: MutableList<MantenimientoCUDItem> = mutableListOf()
        val formato = SimpleDateFormat("yyyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val fechaActual = Date()
        calendar.time = fechaActual
        calendar.add(Calendar.MONTH, -4)

        val fechaLimite = calendar.time

        for (m in mantenimientos) {
            val fecha = formato.parse(m.dia)
            if (fecha.after(fechaLimite) || fecha == fechaLimite)
                last4MM.add(m)
        }
        return last4MM.subList(0, minOf(last4MM.size, 4))
    }
}