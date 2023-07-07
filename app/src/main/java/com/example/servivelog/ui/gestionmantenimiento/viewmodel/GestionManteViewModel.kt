package com.example.servivelog.ui.gestionmantenimiento.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.ComputerUseCase
import com.example.servivelog.domain.LaboratoryUseCase
import com.example.servivelog.domain.MantenimientoUseCase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.domain.TypeMantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionManteViewModel @Inject constructor(
    private val mantenimientoUseCase: MantenimientoUseCase,
    private val laboratoryUseCase: LaboratoryUseCase,
    private val computerUseCase: ComputerUseCase,
    private val readTipoMant: TypeMantUseCase
): ViewModel(){
    val modeloMantenimiento = MutableLiveData<List<MantenimientoCUDItem>>()
    var loading = MutableLiveData<Boolean>()
    val mantenimientoItem = MantenimientoCUDItem(0," ",  "Sin datos", "", "", "")

    fun onCreate(){
        //Funcion para un futuro
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = mantenimientoUseCase()

            if(!resultado.isEmpty()){
                modeloMantenimiento.postValue(resultado)
                loading.postValue(false)
            }else{
                resultado = listOf(mantenimientoItem)
                modeloMantenimiento.postValue(resultado)
                loading.postValue(false)
            }
        }
    }

    fun insertMantenimiento(mantenimientoItem: MantenimientoItem){
        viewModelScope.launch { mantenimientoUseCase.insertMantenimiento(mantenimientoItem) }
    }

    fun updateMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        viewModelScope.launch { mantenimientoUseCase.updateMantenimiento(mantenimientoCUDItem) }
    }

    fun deleteMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        viewModelScope.launch { mantenimientoUseCase.deleteMantenimiento(mantenimientoCUDItem) }
    }

    suspend fun getAllLabs(): List<LabItem>{
        return laboratoryUseCase()
    }

    suspend fun getAllComputers(): List<ComputerItem>{
        return computerUseCase()
    }

    suspend fun tiposDeMant():List<TipoMantItem>{
        return readTipoMant()
    }
}