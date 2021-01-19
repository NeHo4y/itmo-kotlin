package com.github.neho4u.shared.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCreationDto(
    val description: String
)

@Serializable
data class CategoryDto(
    val id: Long,
    val description: String
)
