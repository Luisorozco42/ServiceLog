package com.example.servivelog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.entities.ComputerEntity

@Dao
interface ComputerDao {

    @Query("SELECT * FROM tblComputer ORDER BY idComp")
    suspend fun getAllComputers():List<ComputerEntity>

    @Query("SELECT * FROM tblComputer WHERE idComp = :idC")
    fun getComputerById(idC: Int): ComputerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComputer(computer: ComputerEntity)

    @Update
    suspend fun updateComputer(computer: ComputerEntity)

    @Delete
    suspend fun deleteComputer(computer: ComputerEntity)
}