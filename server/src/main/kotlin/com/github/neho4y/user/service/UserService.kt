package com.github.neho4y.user.service

import com.github.neho4y.user.domain.User
import com.github.neho4y.user.model.UserCreationDto
import com.github.neho4y.user.model.UserUpdateDto

interface UserService {
    suspend fun createUser(userCreationDto: UserCreationDto): User
    suspend fun loginUser(username: String, password: String): User
    suspend fun findByUsername(username: String): User?
    suspend fun deleteUser(username: String)
    suspend fun updateUserInfo(user: User, userUpdateDto: UserUpdateDto): User
}
