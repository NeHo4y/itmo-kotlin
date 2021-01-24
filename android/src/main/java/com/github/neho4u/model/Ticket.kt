package com.github.neho4u.model

import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.feedback.FeedbackStatus
import kotlinx.datetime.LocalDateTime

data class Ticket(
    val id: Long,
    val lastUpdated: LocalDateTime? = null,
    val subject: String? = null,
    val detail: String? = null,
    val displayClient: String? = null,
    val priority: FeedbackPriority? = null,
    val notes: List<Note>? = null,
    val assignee: String? = null,
    val status: FeedbackStatus? = null
)
