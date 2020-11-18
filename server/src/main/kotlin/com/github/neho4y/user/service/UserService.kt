package com.github.neho4y.user.service

import com.github.neho4y.user.domain.User
import com.github.neho4y.user.model.UserCreationDto
import com.github.neho4y.user.model.UserUpdateDto

interface UserService {
    fun createUser(userCreationDto: UserCreationDto): User
    fun loginUser(username: String, password: String): User
    fun findByUsername(username: String): User?
    fun deleteUser(username: String)
    fun updateUserInfo(user: User, userUpdateDto: UserUpdateDto): User
}
