package com.github.neho4u.shared.model.category

import kotlinx.serialization.Serializable

@Serializable
data class SubtopicCreationDto(
    val description: String,
    val topicId: Long
)

@Serializable
data class SubtopicDto(
    val id: Long,
    val description: String,
    val topicId: Long
)
