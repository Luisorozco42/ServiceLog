package com.example.servivelog.domain.labusecase

import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem

import javax.inject.Inject

class RUDLab @Inject constructor(
    private val labRepository: LabRepository
) {

    fun searchLabByN(lab: String): LabItem {
        return labRepository.getLabbyLabN(lab)
    }
    fun getLabById(idL: Int): LabItem{
        return labRepository.getLabById(idL)
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