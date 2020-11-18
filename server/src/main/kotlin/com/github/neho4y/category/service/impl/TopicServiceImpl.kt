package com.github.neho4y.category.service.impl

import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.category.model.TopicCreationDto
import com.github.neho4y.category.model.TopicDto
import com.github.neho4y.category.service.TopicService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.RuntimeException


@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository
) : TopicService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun createTopic(topicCreationDto: TopicCreationDto): Topic {
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicCreationDto.description, topicCreationDto.categoryId)) {
            throw RuntimeException()
        }
        val newTopic = Topic(topicCreationDto.description, topicCreationDto.categoryId)
        val savedTopic = topicRepository.save(newTopic)
        log.info("Topic ${savedTopic.description} is saved under id ${savedTopic.id}")
        return savedTopic
    }

    override fun getAllTopics(): List<Topic> {
        return topicRepository.findAllByIsDeletedFalse()
    }

    override fun getTopic(id: Long) = topicRepository.findByIdAndIsDeletedFalse(id).orElseThrow()

    override fun updateTopic(topicDto: TopicDto) {
        val topic = topicRepository.findByIdAndIsDeletedFalse(topicDto.id).orElseThrow()
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicDto.description, topicDto.categoryId)) {
            log.info("Topic ${topic.description} already exists")
            throw RuntimeException()
        }
        topic.description = topicDto.description
        topic.categoryId = topicDto.categoryId
        topicRepository.save(topic)
        log.info("Topic ${topic.id} is updated with description ${topicDto.description}")
    }

    override fun deleteTopic(id: Long): Topic {
        val topic = topicRepository.findByIdAndIsDeletedFalse(id).orElseThrow()
        return topicRepository.save(topic.copy(isDeleted = true))
    }

}
