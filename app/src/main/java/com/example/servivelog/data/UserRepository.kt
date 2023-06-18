package com.example.servivelog.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.servivelog.data.database.dao.UserDao
import com.example.servivelog.data.database.entities.UserEntity
import com.example.servivelog.domain.model.user.UserItem
import com.example.servivelog.domain.model.user.toDomain
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun getUser(idU: Int): UserItem {
        val resultado: UserEntity = userDao.getUser(idU)

        return resultado.toDomain()
    }


    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)

    }


    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)

    }


    suspend fun deelteUser(user: UserEntity) {
        userDao.deelteUser(user)
    }


     suspend fun getUserByUsername(): List<UserItem> {
        val resultado = userDao.getUserByUsername()

        return resultado.map { it.toDomain() }

    }

}