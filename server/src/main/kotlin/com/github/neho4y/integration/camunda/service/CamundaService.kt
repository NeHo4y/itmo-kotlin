package com.github.neho4y.integration.camunda.service

import com.github.neho4u.shared.model.feedback.FeedbackStatus
import com.github.neho4y.integration.camunda.model.CamundaResult
import com.github.neho4y.user.domain.User

interface CamundaService {
    suspend fun startProcess(feedbackId: Long): CamundaResult
    suspend fun assignFeedback(feedbackId: Long, admin: User): CamundaResult
    suspend fun closeFeedback(feedbackId: Long, admin: User, status: FeedbackStatus): CamundaResult
    suspend fun syncUser(user: User): CamundaResult
    fun isEnabled(): Boolean
}
