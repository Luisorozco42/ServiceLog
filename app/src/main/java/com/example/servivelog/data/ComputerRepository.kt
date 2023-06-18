package com.example.servivelog.data

import com.example.servivelog.data.database.dao.ComputerDao
import com.example.servivelog.data.database.entities.ComputerEntity
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.computer.toDomain
import javax.inject.Inject

class ComputerRepository @Inject constructor(
    private val computerDao: ComputerDao
) {

    suspend fun getAllComputers():List<ComputerItem>{
        val response: List<ComputerEntity> = computerDao.getAllComputers()
        return response.map { it.toDomain() }

    }
      fun getComputerById(idC: Int): ComputerItem{
        val response: ComputerEntity = computerDao.getComputerById(idC)
        return response.toDomain()
    }
    suspend fun insertComputer(computer: ComputerEntity){
        computerDao.insertComputer(computer)
    }
    suspend fun updateComputer(computer: ComputerEntity){
        computerDao.updateComputer(computer)
    }
    suspend fun deleteComputer(computer: ComputerEntity){
        computerDao.deleteComputer(computer)
    }




}