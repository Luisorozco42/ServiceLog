package com.example.servivelog.ui.gestionlaboratorio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.computerusecase.GetAllComputer
import com.example.servivelog.domain.labusecase.GetAllLab
import com.example.servivelog.domain.labusecase.RUDLab
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.lab.InsertLab
import com.example.servivelog.domain.model.lab.LabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestionLabViewModel @Inject constructor(
    private val getAllLab: GetAllLab,
    private val rudLab: RUDLab,
    private val getAllComputer: GetAllComputer
) : ViewModel() {
    val modeloLab = MutableLiveData<List<LabItem>>()
    val loader = MutableLiveData<Boolean>()
    val labItem = LabItem(0, "Sin Datos", "")

    fun onCreate() {
        viewModelScope.launch {
            loader.postValue(true)
            var result = getAllLab()
            if (!result.isEmpty()) {
                modeloLab.postValue(result)
                loader.postValue(false)
            } else {
                result = listOf(labItem)
                modeloLab.postValue(result)
                loader.postValue(false)
            }
        }
    }

    fun getLabById(idL: Int): LabItem {
        return rudLab.getLabById(idL)
    }

    fun insertLab(lab: InsertLab) {
        viewModelScope.launch {
            rudLab.insertLab(lab)
        }
    }

    fun updateLab(lab: LabItem) {
        viewModelScope.launch {
            rudLab.updateLab(lab)
        }
    }

    fun deleteLab(lab: LabItem) {
        viewModelScope.launch {
            rudLab.deleteLab(lab)
        }
    }
    suspend fun getAllLabs(): List<LabItem>{
        return getAllLab()
    }

    suspend fun getAllComps(): List<ComputerItem>{
        return getAllComputer()
    }
}