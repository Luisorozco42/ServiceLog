package com.example.servivelog.ui.gestiondiagnostico.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.ComputerUseCase
import com.example.servivelog.domain.DiagnosisUseCase
import com.example.servivelog.domain.LaboratoryUseCase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis
import com.example.servivelog.domain.model.lab.LabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestioDiagnosisViewModel @Inject constructor(
    private val diagnosisUseCase: DiagnosisUseCase,
    private val computerUseCase: ComputerUseCase,
    private val laboratoryUseCase: LaboratoryUseCase
) : ViewModel() {

    val modeloDiagnosis = MutableLiveData<List<DiagnosisItem>>()
    var loading = MutableLiveData<Boolean>()
    val diagnosisItem = DiagnosisItem(0, "", "Sin datos", "", "", "", "", "","")

    fun onCreate() {
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = diagnosisUseCase()

            if (!resultado.isEmpty()) {
                modeloDiagnosis.postValue(resultado)
                loading.postValue(false)

            } else {
                resultado = listOf(diagnosisItem)
                modeloDiagnosis.postValue(resultado)
                loading.postValue(false)
            }
        }
    }

    fun insertDiagnosi(insertDiagnosis: InsertDiagnosis) {
        viewModelScope.launch {
            diagnosisUseCase.insertDiagnosis(insertDiagnosis)
        }
    }

    fun updateDiagnosis(diagnosisItem: DiagnosisItem) {
        viewModelScope.launch {
            diagnosisUseCase.updateDiagnosis(diagnosisItem)
        }
    }

    fun deleteDiagnosis(diagnosisItem: DiagnosisItem) {
        viewModelScope.launch {
            diagnosisUseCase.deleteDiagnosis(diagnosisItem)
        }
    }

    suspend fun getAllLaboratories(): List<LabItem> {
        return laboratoryUseCase()
    }

    suspend fun getAllComputer(): List<ComputerItem> {
        return computerUseCase()
    }
}