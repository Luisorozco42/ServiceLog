package com.example.servivelog.domain.computerusecase

import com.example.servivelog.data.ComputerRepository
import com.example.servivelog.domain.model.computer.ComputerItem
import javax.inject.Inject

//para preparar la clase es @ + Inject after escribis constructor()
class GetAllComputer @Inject constructor(//preparamos la clase para usar inject
    private val computerRepository: ComputerRepository //asi se injecta la clase repository
) {

   //invoke funciona como una funcion rapida para llamar datos en este caso.
    suspend operator fun invoke(): List<ComputerItem>{
        return computerRepository.getAllComputers()
    }
}