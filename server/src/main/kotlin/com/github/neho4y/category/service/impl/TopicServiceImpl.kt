package com.github.neho4y.category.service.impl

import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.category.model.TopicCreationDto
import com.github.neho4y.category.model.TopicDto
import com.github.neho4y.category.service.TopicService
import com.github.neho4y.common.exception.BasicException
import com.github.neho4y.common.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository
) : TopicService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun createTopic(topicCreationDto: TopicCreationDto): Topic {
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicCreationDto.description, topicCreationDto.categoryId
            )
        ) {
            throw BasicException("Topic ${topicCreationDto.description} already exists")
        }
        val newTopic = Topic(topicCreationDto.description, topicCreationDto.categoryId)
        val savedTopic = topicRepository.save(newTopic)
        log.info("Topic ${savedTopic.description} is saved under id ${savedTopic.id}")
        return savedTopic
    }

    override fun getAllTopics(): List<Topic> {
        return topicRepository.findAllByIsDeletedFalse()
    }

    override fun getTopic(id: Long): Topic = topicRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow { NotFoundException("Unable to find requested topic") }

    override fun updateTopic(topicDto: TopicDto) {
        val topic = topicRepository.findByIdAndIsDeletedFalse(topicDto.id)
            .orElseThrow { NotFoundException("Unable to find requested topic") }
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicDto.description, topicDto.categoryId
            )
        ) {
            val msg = "Topic ${topic.description} already exists"
            log.error(msg)
            throw BasicException(msg)
        }
        topic.description = topicDto.description
        topic.categoryId = topicDto.categoryId
        topicRepository.save(topic)
        log.info("Topic ${topic.id} is updated with description ${topicDto.description}")
    }

    override fun deleteTopic(id: Long): Topic {
        val topic = topicRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { NotFoundException("Unable to find requested topic") }
        return topicRepository.save(topic.copy(isDeleted = true))
    }
}
