package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.model.TopicCreationDto
import com.github.neho4y.category.model.TopicDto

interface TopicService {
    suspend fun getAllTopics(): List<Topic>
    suspend fun createTopic(topicCreationDto: TopicCreationDto): Topic
    suspend fun getTopic(id: Long): Topic
    suspend fun updateTopic(topicDto: TopicDto)
    suspend fun deleteTopic(id: Long): Topic
}
