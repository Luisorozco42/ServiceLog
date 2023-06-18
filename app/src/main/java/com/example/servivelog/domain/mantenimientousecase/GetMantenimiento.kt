package com.example.servivelog.domain.mantenimientousecase

import com.example.servivelog.data.MantenimientoRepository
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
import javax.inject.Inject

class GetMantenimiento @Inject constructor(
    private  val mantenimientoRepository: MantenimientoRepository
) {

    suspend operator fun invoke(): List<MantenimientoCUDItem>{
        return mantenimientoRepository.getAllMantenimientos()
    }
}