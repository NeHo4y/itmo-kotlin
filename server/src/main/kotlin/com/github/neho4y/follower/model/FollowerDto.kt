package com.github.neho4y.follower.model

import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.follower.domain.FeedbackFollowerType
import com.github.neho4y.user.domain.User

data class FollowerDto(
    val feedback: Feedback,
    val user: User,
    val followerType: FeedbackFollowerType
)

data class FollowerFilterDto(
    val feedbackId: Long? = null,
    val userId: Long? = null,
    val followerType: FeedbackFollowerType? = null
)
