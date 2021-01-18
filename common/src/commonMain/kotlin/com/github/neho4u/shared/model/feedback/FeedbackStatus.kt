package com.github.neho4u.shared.model.feedback

import kotlinx.serialization.Serializable

@Serializable
enum class FeedbackStatus {
    CREATED,
    OPEN,
    IN_PROGRESS,
    WAITING,
    CLOSED,
    RESOLVED
}
