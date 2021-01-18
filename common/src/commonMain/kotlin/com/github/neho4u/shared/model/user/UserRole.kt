package com.github.neho4u.shared.model.user

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    USER,
    ADMIN
}
