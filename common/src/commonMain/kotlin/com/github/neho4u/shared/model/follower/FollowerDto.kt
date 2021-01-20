package com.github.neho4u.shared.model.follower

import com.github.neho4u.shared.model.user.UserData
import kotlinx.serialization.Serializable

@Serializable
data class FollowerCreateDto(
    val feedbackId: Long,
    val followerType: FeedbackFollowerType
)

@Serializable
data class FollowerData(
    val feedbackId: Long,
    val user: UserData,
    val followerType: FeedbackFollowerType,
    val id: Long
)

@Serializable
data class FollowerFilterDto(
    val feedbackId: Long? = null,
    val userId: Long? = null,
    val followerType: FeedbackFollowerType? = null
)
