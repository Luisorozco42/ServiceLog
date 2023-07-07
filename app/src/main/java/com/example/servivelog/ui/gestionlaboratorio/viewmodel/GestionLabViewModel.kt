package com.example.servivelog.ui.gestionlaboratorio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.ComputerUseCase
import com.example.servivelog.domain.LaboratoryUseCase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionLabViewModel @Inject constructor(
    private val laboratoryUseCase: LaboratoryUseCase,
    private val computerUseCase: ComputerUseCase
) : ViewModel() {
    val modeloLab = MutableLiveData<List<LabItem>>()
    val loader = MutableLiveData<Boolean>()
    val labItem = LabItem(0, "Sin Datos", "")

    fun onCreate() {
        viewModelScope.launch {
            loader.postValue(true)
            var result = laboratoryUseCase()
            if (!result.isEmpty()) {
                modeloLab.postValue(result)
                loader.postValue(false)
            } else {
                result = listOf(labItem)
                modeloLab.postValue(result)
                loader.postValue(false)
            }
        }
    }

    fun insertLab(lab: InsertLab) {
        viewModelScope.launch {
            laboratoryUseCase.insertLab(lab)
        }
    }

    fun updateLab(lab: LabItem) {
        viewModelScope.launch {
            laboratoryUseCase.updateLab(lab)
        }
    }

    fun deleteLab(lab: LabItem) {
        viewModelScope.launch {
            laboratoryUseCase.deleteLab(lab)
        }
    }

    suspend fun getAllLabs(): List<LabItem>{
        return laboratoryUseCase()
    }

    suspend fun getAllComps(): List<ComputerItem>{
        return computerUseCase()
    }
}