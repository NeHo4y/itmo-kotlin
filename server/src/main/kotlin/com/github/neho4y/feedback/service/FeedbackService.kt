package com.github.neho4y.feedback.service

import com.github.neho4u.shared.model.feedback.*
import com.github.neho4y.user.domain.User

interface FeedbackService {
    suspend fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<FeedbackDto>
    suspend fun createFeedback(userId: Long, feedbackCreationDto: FeedbackCreationDto): FeedbackDto
    suspend fun getFeedback(id: Long): FeedbackDto
    suspend fun updateFeedback(feedbackDto: FeedbackDto, id: Long): FeedbackDto
    suspend fun updateStatus(status: FeedbackStatus, id: Long, user: User)
    suspend fun updatePriority(priority: FeedbackPriority, id: Long)
    suspend fun getFeedbacksByFollower(userId: Long): List<FeedbackDto>
}
