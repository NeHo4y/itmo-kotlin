package com.github.neho4u.shared.model.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(
    val email: String,
    val password: String,
    val username: String,
    val phone: String? = null
)

@Serializable
data class LoginParams(
    val username: String,
    val password: String
)

@Serializable
data class UserToken(
    val token: String
)

@Serializable
data class UserData(
    val username: String,
    val email: String,
    val phone: String?,
    val role: UserRole
)
