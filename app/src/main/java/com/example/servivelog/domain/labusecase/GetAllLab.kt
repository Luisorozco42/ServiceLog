package com.example.servivelog.domain.labusecase

import com.example.servivelog.data.LabRepository
import com.example.servivelog.domain.model.lab.LabItem
import javax.inject.Inject

class GetAllLab @Inject constructor(
    private val labRepository: LabRepository
) {
    suspend operator fun invoke(): List<LabItem>{
        return labRepository.getAllLabs()
    }
}