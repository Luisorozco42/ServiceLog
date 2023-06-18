package com.example.servivelog.domain.diagnosisusecase

import com.example.servivelog.data.DiagnosisRepository
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import javax.inject.Inject

class ReadDiagnosis @Inject constructor(
    private val diagnosisRepository: DiagnosisRepository
) {

    suspend operator fun invoke(): List<DiagnosisItem> {
        return diagnosisRepository.getAllDiagnosis()
    }

}