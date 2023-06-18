package com.example.servivelog.domain.diagnosisusecase

import com.example.servivelog.data.ComputerRepository
import com.example.servivelog.data.DiagnosisRepository
import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.database.entities.toDataBase
import com.example.servivelog.data.database.entities.toInsertDataBase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis
import com.example.servivelog.domain.model.lab.LabItem
import javax.inject.Inject

class CudDiagnosis @Inject constructor(
    private val diagnosis: DiagnosisRepository,
    private val computerRepository: ComputerRepository,
    private val labRepository: LabRepository
) {

    suspend fun getAllComputer(): List<ComputerItem> {

        return computerRepository.getAllComputers()
    }

    suspend fun getallLaboratories(): List<LabItem> {
        return labRepository.getAllLabs()
    }
    suspend fun getAllDiagnosis():List<DiagnosisItem>{
        return  diagnosis.getAllDiagnosis()
    }
    suspend fun insertDiagnosis(insertDiagnosis: InsertDiagnosis) {
        diagnosis.insertDiagnosis(insertDiagnosis.toInsertDataBase())

    }

    suspend fun updateDiagnosis(diagnosisItem: DiagnosisItem) {
        diagnosis.updatetDiagnosis(diagnosisItem.toDataBase())
    }

    suspend fun deleteDiagnosis(diagnosisItem: DiagnosisItem) {
        diagnosis.deleteDiagnosis(diagnosisItem.toDataBase())
    }

    suspend fun getLastFourDiagnosis(): List<DiagnosisItem>{
        return diagnosis.getLastFourDiagnosis()
    }

}