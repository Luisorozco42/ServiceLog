package com.example.servivelog.ui.gestiontipomantenimiento.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.model.tipoMantenimiento.InsertTipoMant
import com.example.servivelog.domain.model.tipoMantenimiento.TipoMantItem
import com.example.servivelog.domain.TypeMantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionTipoMantViewModel @Inject constructor(
    private val typeMantUseCase: TypeMantUseCase
): ViewModel() {

    val modeloTipoMant = MutableLiveData<List<TipoMantItem>>()
    val loading = MutableLiveData<Boolean>()
    val tipoMantItem = TipoMantItem(0,"")

    fun onCreate(){
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = typeMantUseCase()

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
        viewModelScope.launch{ typeMantUseCase.insertTipoMant(insertTipoMant) }
    }

    fun updateTipoMant(tipoMantItem: TipoMantItem){
        viewModelScope.launch { typeMantUseCase.updateTipoMant(tipoMantItem) }
    }

    fun deleteTipoMant(tipoMantItem: TipoMantItem){
        viewModelScope.launch { typeMantUseCase.deleteTipoMant(tipoMantItem) }
    }
}