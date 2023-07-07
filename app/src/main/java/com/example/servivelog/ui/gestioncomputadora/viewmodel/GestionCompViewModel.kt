package com.example.servivelog.ui.gestioncomputadora.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.ComputerUseCase
import com.example.servivelog.domain.DiagnosisUseCase
import com.example.servivelog.domain.MantenimientoUseCase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.computer.InsertItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionCompViewModel @Inject constructor(
    private val computerUseCase: ComputerUseCase,
    private val diagnosisUseCase: DiagnosisUseCase,
    private val mantenimientoUseCase: MantenimientoUseCase
) : ViewModel() {
    val modeloComputer = MutableLiveData<List<ComputerItem>>()
    val loading = MutableLiveData<Boolean>()
    val computerItem = ComputerItem(
        0,
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        "sin datos",
        "",
        ""
    )

    fun onCreate() {
        viewModelScope.launch {//Se usa una corrutina para conectar el viewmodel
            loading.postValue(true)//permite postear durante la carga
            var resultado = computerUseCase()
            if (!resultado.isEmpty()) {
                modeloComputer.postValue(resultado)
                loading.postValue(false)
            } else {
                resultado = listOf(computerItem)
                modeloComputer.postValue(resultado)
                loading.postValue(false)
            }
        }
    }

    fun insertComputer(computer: InsertItem) {
        viewModelScope.launch {
            computerUseCase.insertComputer(computer)
        }
    }

    fun updateComputer(computer: ComputerItem) {
        viewModelScope.launch {
            computerUseCase.updateComputer(computer)
        }
    }

    fun deleteComputer(computer: ComputerItem) {
        viewModelScope.launch {
            computerUseCase.deleteComputer(computer)
        }
    }

    suspend fun getAllLabs(): List<LabItem> {
        return computerUseCase.getallLabs()

    }

    suspend fun getComputers(): List<ComputerItem> {
        return computerUseCase()
    }

    suspend fun getAllDiagnosis(): List<DiagnosisItem>{
        return diagnosisUseCase()
    }

    suspend fun getAllMantenimientos(): List<MantenimientoCUDItem>{
        return mantenimientoUseCase()
    }
}