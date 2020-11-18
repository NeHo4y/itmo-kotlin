package com.github.neho4y.category.service.impl

import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.domain.repository.SubtopicRepository
import com.github.neho4y.category.model.SubtopicCreationDto
import com.github.neho4y.category.model.SubtopicDto
import com.github.neho4y.category.service.SubtopicService
import com.github.neho4y.common.exception.BasicException
import com.github.neho4y.common.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class SubtopicServiceImpl(
    private val subtopicRepository: SubtopicRepository
) : SubtopicService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun createSubtopic(subtopicCreationDto: SubtopicCreationDto): Subtopic {
        if (subtopicRepository.existsByDescriptionAndIsDeletedFalseAndTopicId(
                subtopicCreationDto.description, subtopicCreationDto.topicId
            )
        ) {
            throw BasicException("Subtopic ${subtopicCreationDto.description} already exists")
        }
        val newSubtopic = Subtopic(subtopicCreationDto.description, subtopicCreationDto.topicId)
        val savedSubtopic = subtopicRepository.save(newSubtopic)
        log.info("Subtopic ${savedSubtopic.description} is saved under id ${savedSubtopic.id}")
        return savedSubtopic
    }

    override fun getSubtopic(id: Long): Subtopic = subtopicRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow { NotFoundException("Unable to find requested subtopic") }

    override fun updateSubtopic(subtopicDto: SubtopicDto) {
        val subtopic = subtopicRepository.findByIdAndIsDeletedFalse(subtopicDto.id)
            .orElseThrow { NotFoundException("Unable to find requested subtopic") }
        if (subtopicRepository.existsByDescriptionAndIsDeletedFalseAndTopicId(
                subtopicDto.description, subtopicDto.topicId
            )
        ) {
            val msg = "Subtopic ${subtopic.description} already exists"
            log.error(msg)
            throw BasicException(msg)
        }
        subtopic.description = subtopicDto.description
        subtopic.topicId = subtopicDto.topicId
        subtopicRepository.save(subtopic)
        log.info("Subtopic ${subtopic.id} is updated with description ${subtopicDto.description}")
    }

    override fun getAllSubtopics(): List<Subtopic> {
        return subtopicRepository.findAllByIsDeletedFalse()
    }

    override fun deleteSubtopic(id: Long): Subtopic {
        val subtopic = subtopicRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow { NotFoundException("Unable to find requested subtopic") }
        return subtopicRepository.save(subtopic.copy(isDeleted = true))
    }

}
