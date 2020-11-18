package com.github.neho4y.user.model

data class UserCreationDto(
    val email: String,
    val password: String,
    val username: String,
    val phone: String? = null
)

data class UserUpdateDto(
    val phone: String? = null
)
