package com.example.servivelog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.entities.TipodMantenimientoEntity

@Dao
interface TipoMantenimientoDao {

    @Query("SELECT * FROM tbl_tipoMant ORDER BY idTM")
    suspend fun getAllTipoMant(): List<TipodMantenimientoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity)

    @Update
    suspend fun updateTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity)

    @Delete
    suspend fun deleteTipoMant(tipodMantenimientoEntity: TipodMantenimientoEntity)

}