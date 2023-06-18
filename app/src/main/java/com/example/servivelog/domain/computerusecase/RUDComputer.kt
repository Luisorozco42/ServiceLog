package com.example.servivelog.domain.computerusecase

import com.example.servivelog.data.ComputerRepository
import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.computer.InsertItem
import com.example.servivelog.domain.model.lab.LabItem
import javax.inject.Inject

class RUDComputer @Inject constructor(
    private val computerRepository: ComputerRepository,
    private val labRepository: LabRepository
) {
    suspend fun getallLabs(): List<LabItem> {
        return labRepository.getAllLabs()
    }

    //mando a llamar a la pc atraves del id , abajo declaro la funcion similar
    //a la fun CRespository
    fun getComputerById(idC: Int): ComputerItem {
        return computerRepository.getComputerById(idC)
    }

    //insertamos en la bd y usamos un mapper para acomadar los dtos en la otra dataClass
    suspend fun insertComputer(computer: InsertItem) {
        computerRepository.insertComputer(computer.toDatabase())
    }

    //en este caso mandamos a llamar un Computer item
    suspend fun updateComputer(computer: ComputerItem) {
        computerRepository.updateComputer(computer.toDatabase())
    }

    suspend fun deleteComputer(computer: ComputerItem) {
        computerRepository.deleteComputer(computer.toDatabase())
    }
}