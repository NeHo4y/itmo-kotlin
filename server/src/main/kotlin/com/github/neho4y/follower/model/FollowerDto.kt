package com.github.neho4y.follower.model

import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4y.user.domain.User

data class FollowerDto(
    val feedbackId: Long,
    val user: User,
    val followerType: FeedbackFollowerType
)
