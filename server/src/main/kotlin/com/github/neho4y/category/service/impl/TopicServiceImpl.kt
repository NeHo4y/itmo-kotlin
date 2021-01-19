package com.github.neho4y.category.service.impl

import com.github.neho4u.shared.model.category.TopicCreationDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4y.category.controller.toDto
import com.github.neho4y.category.domain.Topic
import com.github.neho4y.category.domain.repository.TopicRepository
import com.github.neho4y.category.service.TopicService
import com.github.neho4y.common.exception.AlreadyExistsException
import com.github.neho4y.common.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository
) : TopicService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun createTopic(topicCreationDto: TopicCreationDto): TopicDto {
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicCreationDto.description, topicCreationDto.categoryId
            )
        ) {
            throw AlreadyExistsException("Topic ${topicCreationDto.description} already exists")
        }
        val newTopic = Topic(topicCreationDto.description, topicCreationDto.categoryId)
        val savedTopic = topicRepository.save(newTopic)
        log.info("Topic ${savedTopic.description} is saved under id ${savedTopic.id}")
        return savedTopic.toDto()
    }

    override suspend fun getAllTopics(): List<TopicDto> {
        return topicRepository.findAllByIsDeletedFalse().stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    }

    override suspend fun getTopic(id: Long): TopicDto = topicRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow { NotFoundException("Unable to find requested topic") }.toDto()

    override suspend fun updateTopic(topicDto: TopicDto) {
        val topic = topicRepository.findByIdAndIsDeletedFalse(topicDto.id)
            .orElseThrow { NotFoundException("Unable to find requested topic") }
        if (topicRepository.existsByDescriptionAndIsDeletedFalseAndCategoryId(
                topicDto.description, topicDto.categoryId
            )
        ) {
            val msg = "Topic ${topic.description} already exists"
            log.error(msg)
            throw AlreadyExistsException(msg)
        }
        topic.description = topicDto.description
        topic.categoryId = topicDto.categoryId
        topicRepository.save(topic)
        log.info("Topic ${topic.id} is updated with description ${topicDto.description}")
    }

    override suspend fun getTopicsByCategoryId(categoryId: Long): List<TopicDto> {
        return topicRepository.findAllByCategoryIdAndIsDeletedFalse(categoryId).stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    }

    override suspend fun deleteTopic(id: Long): TopicDto {
        val topic = topicRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { NotFoundException("Unable to find requested topic") }
        return topicRepository.save(topic.copy(isDeleted = true)).toDto()
    }
}
