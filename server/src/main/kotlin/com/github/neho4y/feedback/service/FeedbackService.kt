package com.github.neho4y.feedback.service

import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.FeedbackPriority
import com.github.neho4y.feedback.domain.FeedbackStatus
import com.github.neho4y.feedback.model.FeedbackCreationDto
import com.github.neho4y.feedback.model.FeedbackDto
import com.github.neho4y.feedback.model.FeedbackFilter

interface FeedbackService {
    suspend fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<Feedback>
    suspend fun createFeedback(feedbackCreationDto: FeedbackCreationDto): Feedback
    suspend fun getFeedback(id: Long): Feedback
    suspend fun updateFeedback(feedbackDto: FeedbackDto): Feedback
    suspend fun updateStatus(status: FeedbackStatus, id: Long)
    suspend fun updatePriority(priority: FeedbackPriority, id: Long)
}
