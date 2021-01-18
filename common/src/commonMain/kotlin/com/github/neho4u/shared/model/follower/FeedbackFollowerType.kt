package com.github.neho4u.shared.model.follower

import kotlinx.serialization.Serializable

@Serializable
enum class FeedbackFollowerType {
    ASSIGNEE,
    WATCHER
}
