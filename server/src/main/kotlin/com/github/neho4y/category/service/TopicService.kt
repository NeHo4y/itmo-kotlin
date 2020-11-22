package com.github.neho4y.category.service

import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.model.TopicCreationDto
import com.github.neho4y.category.model.TopicDto

interface TopicService {

    fun getAllTopics(): List<Topic>
    fun createTopic(topicCreationDto: TopicCreationDto): Topic
    fun getTopic(id: Long): Topic
    fun updateTopic(topicDto: TopicDto)
    fun deleteTopic(id: Long): Topic
}
