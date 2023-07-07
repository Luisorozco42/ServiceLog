package com.example.servivelog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.entities.MantenimientoEntity

@Dao
interface MantenimientoDao {
    @Query("SELECT * FROM tblMantenimiento ORDER BY idM")
    suspend fun getAllMaintenances(): List<MantenimientoEntity>

    @Query("SELECT * FROM tblMantenimiento ORDER BY idM DESC")
    suspend fun getLastfourMaintenances(): List<MantenimientoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(mantenimiento: MantenimientoEntity)

    @Update
    suspend fun updateMaintenance(mantenimiento: MantenimientoEntity)

    @Delete
    suspend fun deleteMaintenance(mantenimiento: MantenimientoEntity)
}