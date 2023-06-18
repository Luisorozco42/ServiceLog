package com.example.servivelog.domain.tipoMantenimientoUseCase

import androidx.lifecycle.LiveData
import com.example.servivelog.data.TipoMantenimientoRepository
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import javax.inject.Inject

class ReadTipoMant @Inject constructor(
    private val repository: TipoMantenimientoRepository
){
    suspend operator fun invoke(): List<TipoMantItem>{
        val data = repository.getAllTipoMantenimiento()
        return data
    }
}