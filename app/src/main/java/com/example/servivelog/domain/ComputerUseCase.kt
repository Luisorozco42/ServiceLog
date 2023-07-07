package com.example.servivelog.domain

import com.example.servivelog.data.ComputerRepository
import com.example.servivelog.data.LabRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.computer.InsertItem
import com.example.servivelog.domain.model.lab.LabItem
import javax.inject.Inject

//para preparar la clase es @ + Inject after escribis constructor()
class ComputerUseCase @Inject constructor(//preparamos la clase para usar inject
    private val computerRepository: ComputerRepository, //asi se injecta la clase repository
    private val labRepository: LabRepository
) {
   //invoke funciona como una funcion rapida para llamar datos en este caso.
    suspend operator fun invoke(): List<ComputerItem>{
        return computerRepository.getAllComputers()
    }

    suspend fun getallLabs(): List<LabItem> {
        return labRepository.getAllLabs()
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