package com.github.neho4u.shared.model.category

import kotlinx.serialization.Serializable

@Serializable
data class TopicCreationDto(
    val description: String,
    val categoryId: Long
)

@Serializable
data class TopicDto(
    val id: Long,
    val description: String,
    val categoryId: Long
)
