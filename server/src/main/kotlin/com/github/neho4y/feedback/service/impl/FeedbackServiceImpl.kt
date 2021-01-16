package com.github.neho4y.feedback.service.impl

import com.github.neho4u.shared.model.feedback.*
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.feedback.domain.repository.FeedbackSearchSpecification
import com.github.neho4y.feedback.domain.repository.FeedbackSpecificationRepository
import com.github.neho4y.feedback.service.FeedbackService
import com.github.neho4y.user.controller.toUserData
import com.github.neho4y.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FeedbackServiceImpl(
    private val feedbackSpecificationRepository: FeedbackSpecificationRepository,
    private val feedbackRepository: FeedbackRepository,
    private val userService: UserService
) : FeedbackService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<FeedbackDto> {
        return feedbackSpecificationRepository.findAll(FeedbackSearchSpecification(feedbackFilter))
            .map { convertToDto(it) }
    }

    override suspend fun createFeedback(userId: Long, feedbackCreationDto: FeedbackCreationDto): FeedbackDto {
        val currentDateTime = LocalDateTime.now()
        val newFeedback = Feedback(
            feedbackCreationDto.header,
            currentDateTime,
            currentDateTime,
            feedbackCreationDto.categoryId,
            feedbackCreationDto.topicId,
            feedbackCreationDto.subtopicId,
            userId
        )
        val savedFeedback = feedbackRepository.save(newFeedback)
        log.info("new feedback with id: ${savedFeedback.id} is saved")
        return convertToDto(savedFeedback)
    }

    override suspend fun getFeedback(id: Long): FeedbackDto {
        return convertToDto(
            feedbackRepository.findById(id)
                .orElseThrow { NotFoundException("Unable to find feedback") }
        )
    }

    override suspend fun updateFeedback(feedbackDto: FeedbackDto, id: Long): FeedbackDto {
        val feedback = feedbackRepository.findById(id)
            .orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.apply {
            header = feedbackDto.header ?: header
            categoryId = feedbackDto.categoryId ?: categoryId
            topicId = feedbackDto.topicId ?: topicId
            subtopicId = feedbackDto.subtopicId ?: subtopicId
            status = feedbackDto.status ?: status
            priority = feedbackDto.priority ?: priority
            updateDate = LocalDateTime.now()
        }
        val updatedFeedback = feedbackRepository.save(feedback)
        return convertToDto(updatedFeedback)
    }

    override suspend fun updateStatus(status: FeedbackStatus, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.status = status
        if (FeedbackStatus.CLOSED == status || FeedbackStatus.RESOLVED == status) {
            feedback.endDate = LocalDateTime.now()
        }
        feedbackRepository.save(feedback)
    }

    override suspend fun updatePriority(priority: FeedbackPriority, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.priority = priority
        feedbackRepository.save(feedback)
    }

    suspend fun convertToDto(feedback: Feedback): FeedbackDto {
        val userData = userService.findById(feedback.authorId).toUserData()
        return FeedbackDto(
            id = feedback.id,
            header = feedback.header,
            categoryId = feedback.categoryId,
            topicId = feedback.topicId,
            subtopicId = feedback.subtopicId,
            status = feedback.status,
            priority = feedback.priority,
            authorData = userData
        )
    }
}
