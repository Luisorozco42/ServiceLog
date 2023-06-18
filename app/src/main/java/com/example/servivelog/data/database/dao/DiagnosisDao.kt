package com.example.servivelog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.entities.DiagnosisEntity

@Dao
interface DiagnosisDao {

    @Query("SELECT * FROM tblDiagnosis ORDER BY idDiag")
    suspend fun getAllDiagnosis(): List<DiagnosisEntity>

    @Query("SELECT * FROM tblDiagnosis ORDER BY idDiag DESC")
    suspend fun  getLastFourDiagnosis(): List<DiagnosisEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: DiagnosisEntity)

    @Update
    suspend fun updatetDiagnosis(diagnosis: DiagnosisEntity)

    @Delete
    suspend fun deleteDiagnosis(diagnosis: DiagnosisEntity)
}