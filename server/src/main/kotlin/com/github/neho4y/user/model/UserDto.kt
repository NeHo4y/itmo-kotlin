package com.github.neho4y.user.model

import com.github.neho4u.shared.model.user.UserRole

data class UserCreationDto(
    val email: String,
    val password: String,
    val username: String,
    val phone: String? = null,
    val role: UserRole = UserRole.USER
)

data class UserUpdateDto(
    val phone: String? = null
)
