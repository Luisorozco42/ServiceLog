package com.example.servivelog.ui.usuario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.domain.DiagnosisUseCase
import com.example.servivelog.domain.MantenimientoUseCase
import com.example.servivelog.domain.model.diagnosis.DiagnosisItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.domain.model.user.InsertUser
import com.example.servivelog.domain.model.user.UserItem
import com.example.servivelog.domain.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val mantenimientoUseCase: MantenimientoUseCase,
    private val diagnosisUseCase: DiagnosisUseCase
) : ViewModel() {

    fun getUser(idU: Int): UserItem {
        return userUseCase.getUser(idU)
    }


    fun insertUser(user: InsertUser) {
        viewModelScope.launch {
            userUseCase.insertUser(user)
        }
    }

    fun updateUser(user: UserItem) {
        viewModelScope.launch {
            userUseCase.updateUser(user)
        }
    }

    fun deelteUser(user: UserItem) {
        viewModelScope.launch {
            userUseCase.deelteUser(user)
        }

    }

     suspend fun getUserByUsername():List<UserItem> {
        return userUseCase.getUserByUsername()
    }

    suspend fun getAllmantenimiento():List<MantenimientoCUDItem>{//funcion obtener lista de todos los mantenimientos
        return mantenimientoUseCase()
    }

    suspend fun getAllDiagnosis(): List<DiagnosisItem>{
        return diagnosisUseCase()
    }

    suspend fun getLastMaintenance(): List<MantenimientoCUDItem> {
        return mantenimientoUseCase.getLastMaintenance()
    }

    suspend fun getlastDiagnosis():List<DiagnosisItem>{
        return diagnosisUseCase.getLastFourDiagnosis()
    }
}
