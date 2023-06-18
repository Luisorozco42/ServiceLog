package com.example.servivelog.ui.gestionmantenimiento.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.computerusecase.RUDComputer
import com.example.servivelog.domain.labusecase.RUDLab
import com.example.servivelog.domain.mantenimientousecase.CUDMantenimiento
import com.example.servivelog.domain.mantenimientousecase.GetMantenimiento
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoItem
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.domain.tipoMantenimientoUseCase.ReadTipoMant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionManteViewModel @Inject constructor(
    private val getMantenimiento: GetMantenimiento,
    private val cudMantenimiento: CUDMantenimiento,
    private val readTipoMant: ReadTipoMant
): ViewModel(){
    val modeloMantenimiento = MutableLiveData<List<MantenimientoCUDItem>>()
    var loading = MutableLiveData<Boolean>()
    val mantenimientoItem = MantenimientoCUDItem(0," ",  "Sin datos", "", "", "")

    fun onCreate(){
        //Funcion para un futuro
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = getMantenimiento()

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
        viewModelScope.launch { cudMantenimiento.insertMantenimiento(mantenimientoItem) }
    }
    fun updateMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        viewModelScope.launch { cudMantenimiento.updateMantenimiento(mantenimientoCUDItem) }
    }
    fun deleteMantenimiento(mantenimientoCUDItem: MantenimientoCUDItem){
        viewModelScope.launch { cudMantenimiento.deleteMantenimiento(mantenimientoCUDItem) }
    }
    suspend fun getAllLabs(): List<LabItem>{
        return cudMantenimiento.getAllLaboratories()
    }
    suspend fun getAllComputers(): List<ComputerItem>{
        return cudMantenimiento.getAllComputers()
    }
    suspend fun tiposDeMant():List<TipoMantItem>{
        return readTipoMant()
    }
}