package com.github.neho4y.feedback.service.impl

import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.FeedbackPriority
import com.github.neho4y.feedback.domain.FeedbackStatus
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.feedback.model.FeedbackCreationDto
import com.github.neho4y.feedback.model.FeedbackDto
import com.github.neho4y.feedback.model.FeedbackFilter
import com.github.neho4y.feedback.service.FeedbackService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FeedbackServiceImpl(
    private val feedbackRepository: FeedbackRepository
) : FeedbackService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<Feedback> {
        return feedbackRepository.findAll()
    }

    override fun createFeedback(feedbackCreationDto: FeedbackCreationDto): Feedback {
        val currentDateTime = LocalDateTime.now()
        val newFeedback = Feedback(
            feedbackCreationDto.header,
            currentDateTime,
            currentDateTime,
            feedbackCreationDto.categoryId,
            feedbackCreationDto.topicId,
            feedbackCreationDto.subtopicId
        )
        val savedFeedback = feedbackRepository.save(newFeedback)
        log.info("new feedback with id: ${savedFeedback.id} is saved")
        return savedFeedback
    }

    override fun getFeedback(id: Long): Feedback {
        return feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
    }

    override fun updateFeedback(feedbackDto: FeedbackDto): Feedback {
        val feedback = feedbackRepository.findById(feedbackDto.id)
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
        return updatedFeedback
    }

    override fun updateStatus(status: FeedbackStatus, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.status = status
        if (FeedbackStatus.CLOSED == status || FeedbackStatus.RESOLVED == status) {
            feedback.endDate = LocalDateTime.now()
        }
        feedbackRepository.save(feedback)
    }

    override fun updatePriority(priority: FeedbackPriority, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.priority = priority
        feedbackRepository.save(feedback)
    }
}
