package com.example.servivelog.ui.gestiondiagnostico.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.diagnosisusecase.CudDiagnosis
import com.example.servivelog.domain.diagnosisusecase.ReadDiagnosis
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis
import com.example.servivelog.domain.model.lab.LabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestioDiagnosisViewModel @Inject constructor(
    private val readDiagnosis: ReadDiagnosis,
    private val cudDiagnosis: CudDiagnosis
) : ViewModel() {

    val modeloDiagnosis = MutableLiveData<List<DiagnosisItem>>()
    var loading = MutableLiveData<Boolean>()
    val diagnosisItem = DiagnosisItem(0, "", "Sin datos", "", "", "", "", "","")

    fun onCreate() {
        viewModelScope.launch {
            loading.postValue(true)
            var resultado = readDiagnosis()

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
            cudDiagnosis.insertDiagnosis(insertDiagnosis)
        }
    }

    fun updateDiagnosis(diagnosisItem: DiagnosisItem) {
        viewModelScope.launch {
            cudDiagnosis.updateDiagnosis(diagnosisItem)
        }
    }

    fun deleteDiagnosis(diagnosisItem: DiagnosisItem) {
        viewModelScope.launch {
            cudDiagnosis.deleteDiagnosis(diagnosisItem)
        }
    }

    suspend fun getAllLaboratories(): List<LabItem> {
        return cudDiagnosis.getallLaboratories()
    }

    suspend fun getAllComputer(): List<ComputerItem> {
        return cudDiagnosis.getAllComputer()
    }



}