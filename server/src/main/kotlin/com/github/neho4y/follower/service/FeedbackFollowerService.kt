package com.github.neho4y.follower.service

import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.model.FollowerFilterDto

interface FeedbackFollowerService {
    suspend fun findFollowsByFilter(filter: FollowerFilterDto): List<FeedbackFollower>
    suspend fun addFollowerToFeedback(creationDto: FollowerDto): FeedbackFollower
    suspend fun removeFollowerFromFeedback(followId: Long)
}
