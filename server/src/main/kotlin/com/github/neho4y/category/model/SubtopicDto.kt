package com.github.neho4y.category.model

data class SubtopicCreationDto(

    val description: String,
    val topicId: Long
)

data class SubtopicDto(

    val id: Long,
    val description: String,
    val topicId: Long

)
