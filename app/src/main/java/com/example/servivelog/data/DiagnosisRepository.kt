package com.example.servivelog.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.dao.DiagnosisDao
import com.example.servivelog.data.database.entities.DiagnosisEntity
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.toDomain
import javax.inject.Inject

class DiagnosisRepository @Inject constructor(
    private val diagnosisDao: DiagnosisDao
) {


    suspend fun getAllDiagnosis(): List<DiagnosisItem> {
        val response: List<DiagnosisEntity> = diagnosisDao.getAllDiagnosis()
        return response.map { it.toDomain() }
    }

    suspend fun getLastFourDiagnosis(): List<DiagnosisItem>{
        val response: List<DiagnosisEntity> = diagnosisDao.getLastFourDiagnosis()
        return response.map{it.toDomain() }
    }

    suspend fun insertDiagnosis(diagnosis: DiagnosisEntity) {
        diagnosisDao.insertDiagnosis(diagnosis)
    }


    suspend fun updatetDiagnosis(diagnosis: DiagnosisEntity) {
        diagnosisDao.updatetDiagnosis(diagnosis)
    }


    suspend fun deleteDiagnosis(diagnosis: DiagnosisEntity) {
        diagnosisDao.deleteDiagnosis(diagnosis)
    }
}