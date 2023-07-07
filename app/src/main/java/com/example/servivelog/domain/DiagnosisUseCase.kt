package com.example.servivelog.domain

import com.example.servivelog.data.DiagnosisRepository
import com.example.servivelog.data.database.entities.toDataBase
import com.example.servivelog.data.database.entities.toInsertDataBase
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.diagnosis.InsertDiagnosis
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DiagnosisUseCase @Inject constructor(
    private val diagnosis: DiagnosisRepository
) {
    suspend operator fun invoke(): List<DiagnosisItem> {
        return diagnosis.getAllDiagnosis()
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

        val diagnosticos: List<DiagnosisItem> = diagnosis.getLastFourDiagnosis()
        val formato = SimpleDateFormat("yyyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val fechaActual = Date()
        calendar.time = fechaActual
        calendar.add(Calendar.MONTH, -1)

        val fechaLimite = calendar.time
        val lastMD: MutableList<DiagnosisItem> = mutableListOf()

        for (d in diagnosticos) {
            val fecha = formato.parse(d.fecha)
            if (fecha.after(fechaLimite) || fecha == fechaLimite ) {
                lastMD.add(d)
            }
        }
        return lastMD.subList(0, minOf(lastMD.size, 4))
    }

}