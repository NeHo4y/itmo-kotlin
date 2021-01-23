package com.github.neho4y.feedback.service

import com.github.neho4u.shared.model.feedback.*

interface FeedbackService {
    suspend fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<FeedbackDto>
    suspend fun createFeedback(userId: Long, feedbackCreationDto: FeedbackCreationDto): FeedbackDto
    suspend fun getFeedback(id: Long): FeedbackDto
    suspend fun updateFeedback(feedbackDto: FeedbackDto, id: Long): FeedbackDto
    suspend fun updateStatus(status: FeedbackStatus, id: Long)
    suspend fun updatePriority(priority: FeedbackPriority, id: Long)
    suspend fun getFeedbacksByFollower(userId: Long, filter: FeedbackFilter): List<FeedbackDto>
}
