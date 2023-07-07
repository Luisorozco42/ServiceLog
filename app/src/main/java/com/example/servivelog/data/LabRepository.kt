package com.example.servivelog.data

import com.example.servivelog.data.database.dao.LabDao
import com.example.servivelog.data.database.entities.LabEntity
import com.example.servivelog.domain.model.lab.LabItem
import com.example.servivelog.domain.model.lab.toDomain
import javax.inject.Inject

class LabRepository @Inject constructor(
    private var labDao: LabDao
) {
    fun getLabbyLabN(lab: String): LabItem {
        val response: LabEntity = labDao.getLabByLabN(lab)
        return response.toDomain()
    }

    suspend fun getAllLabs(): List<LabItem>{
        val response: List<LabEntity> = labDao.getAllLabs()
        return response.map { it.toDomain() }
    }

    suspend fun insertLab(lab: LabEntity){
        labDao.insertLab(lab)
    }

    suspend fun updateLab(lab: LabEntity){
        labDao.updateLab(lab)
    }

    suspend fun deleteLab(lab: LabEntity){
        labDao.deleteLab(lab)
    }
}