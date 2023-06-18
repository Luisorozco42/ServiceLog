package com.example.servivelog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM tblUser WHERE idU = :idU")
    fun getUser(idU: Int): UserEntity

    @Query("SELECT * FROM tblUser ORDER BY idU")
   suspend fun getUserByUsername(): List<UserEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deelteUser(user: UserEntity)
}