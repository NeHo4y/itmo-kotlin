package com.github.neho4y.category.service

import com.github.neho4u.shared.model.category.TopicCreationDto
import com.github.neho4u.shared.model.category.TopicDto

interface TopicService {
    suspend fun getAllTopics(): List<TopicDto>
    suspend fun createTopic(topicCreationDto: TopicCreationDto): TopicDto
    suspend fun getTopic(id: Long): TopicDto
    suspend fun updateTopic(topicDto: TopicDto)
    suspend fun deleteTopic(id: Long): TopicDto
    suspend fun getTopicsByCategoryId(categoryId: Long): List<TopicDto>
}
