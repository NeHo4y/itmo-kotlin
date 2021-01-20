package com.github.neho4y.follower.service

import com.github.neho4u.shared.model.follower.FollowerData
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4y.follower.model.FollowerDto

interface FeedbackFollowerService {
    suspend fun findFollowsByFilter(filter: FollowerFilterDto): List<FollowerData>
    suspend fun addFollowerToFeedback(creationDto: FollowerDto): FollowerData
    suspend fun removeFollowerFromFeedback(follow: FollowerDto)
}
