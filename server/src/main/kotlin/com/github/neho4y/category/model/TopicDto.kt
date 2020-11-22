package com.github.neho4y.category.model

data class TopicCreationDto(

    val description: String,
    val categoryId: Long
)

data class TopicDto(

    val id: Long,
    val description: String,
    val categoryId: Long

)
