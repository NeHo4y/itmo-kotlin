package com.github.neho4y.follower.service

import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.model.FollowerFilterDto

interface FeedbackFollowerService {
    fun findFollowsByFilter(filter: FollowerFilterDto): List<FeedbackFollower>
    fun addFollowerToFeedback(creationDto: FollowerDto): FeedbackFollower
    fun removeFollowerFromFeedback(followId: Long)
}
