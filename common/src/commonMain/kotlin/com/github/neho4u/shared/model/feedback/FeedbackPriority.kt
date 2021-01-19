package com.github.neho4u.shared.model.feedback

import kotlinx.serialization.Serializable

@Serializable
enum class FeedbackPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL,
    GODLIKE
}
