package com.github.neho4y.follower.service

import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerDto

interface FeedbackFollowerService {
    suspend fun findFollowsByFilter(filter: FollowerFilterDto): List<FeedbackFollower>
    suspend fun addFollowerToFeedback(creationDto: FollowerDto): FeedbackFollower
    suspend fun removeFollowerFromFeedback(follow: FollowerDto)
}
