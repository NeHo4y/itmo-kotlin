package com.github.neho4y.feedback.service

import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.FeedbackPriority
import com.github.neho4y.feedback.domain.FeedbackStatus
import com.github.neho4y.feedback.model.FeedbackCreationDto
import com.github.neho4y.feedback.model.FeedbackDto
import com.github.neho4y.feedback.model.FeedbackFilter

interface FeedbackService {
    fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<Feedback>
    fun createFeedback(feedbackCreationDto: FeedbackCreationDto): Feedback
    fun getFeedback(id: Long): Feedback
    fun updateFeedback(feedbackDto: FeedbackDto): Feedback
    fun updateStatus(status: FeedbackStatus, id: Long)
    fun updatePriority(priority: FeedbackPriority, id: Long)
}
