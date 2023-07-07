package com.example.servivelog.domain

import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem
import javax.inject.Inject

class LaboratoryUseCase @Inject constructor(
    private val labRepository: LabRepository
) {
    suspend operator fun invoke(): List<LabItem>{
        return labRepository.getAllLabs()
    }

    suspend fun insertLab(lab: InsertLab){
        labRepository.insertLab(lab.toInsertDatabase())
    }

    suspend fun updateLab(lab: LabItem){
        labRepository.updateLab(lab.toDatabase())
    }

    suspend fun deleteLab(lab: LabItem){
        labRepository.deleteLab(lab.toDatabase())
    }
}