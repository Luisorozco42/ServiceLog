package com.example.servivelog.ui.gestiontipomantenimiento.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.model.tipoMantenimiento.InsertTipoMant
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.domain.tipoMantenimientoUseCase.CUD
import com.example.servivelog.domain.tipoMantenimientoUseCase.ReadTipoMant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionTipoMantViewModel @Inject constructor(
    private val cud: CUD,
    private val readTipoMant: ReadTipoMant
): ViewModel() {

    val modeloTipoMant = MutableLiveData<List<TipoMantItem>>()
    val loading = MutableLiveData<Boolean>()
    val tipoMantItem = TipoMantItem(0,"")

    fun onCreate(){
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = readTipoMant()

            if (!resultado.isEmpty()){
                modeloTipoMant.postValue(resultado)
                loading.postValue(false)
            }else{
                resultado = listOf(tipoMantItem)
                modeloTipoMant.postValue(resultado)
                loading.postValue(false)
            }
        }
    }

    fun insertTipoMant(insertTipoMant: InsertTipoMant){
        viewModelScope.launch{ cud.insertTipoMant(insertTipoMant) }
    }

    fun updateTipoMant(tipoMantItem: TipoMantItem){
        viewModelScope.launch { cud.updateTipoMant(tipoMantItem) }
    }

    fun deleteTipoMant(tipoMantItem: TipoMantItem){
        viewModelScope.launch { cud.deleteTipoMant(tipoMantItem) }
    }

}