package com.example.servivelog.domain

import com.example.servivelog.data.UserRepository
import com.example.servivelog.data.database.entities.toDatabase
import com.example.servivelog.data.database.entities.toInsertDatabase
import com.example.servivelog.domain.model.user.InsertUser
import com.example.servivelog.domain.model.user.UserItem
import javax.inject.Inject


class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getUser(idU: Int): UserItem {

        return userRepository.getUser(idU)
    }

    suspend fun insertUser(user: InsertUser) {
        userRepository.insertUser(user.toInsertDatabase())
    }

    suspend fun updateUser(user: UserItem) {
        userRepository.updateUser(user.toDatabase())
    }

    suspend fun deelteUser(user: UserItem) {
        userRepository.deelteUser(user.toDatabase())
    }

   suspend fun getUserByUsername(): List<UserItem> {
        return userRepository.getUserByUsername()
    }
}