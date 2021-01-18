package com.github.neho4y.follower.controller

import com.github.neho4u.shared.model.follower.FollowerCreateDto
import com.github.neho4u.shared.model.follower.FollowerData
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.service.FeedbackFollowerService
import com.github.neho4y.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feedback/follower")
class FollowerController(private val followerService: FeedbackFollowerService) {

    @PostMapping("/filter")
    suspend fun findFollowsByFilter(@RequestBody filter: FollowerFilterDto): List<FollowerData> {
        return followerService.findFollowsByFilter(filter)
            .map { it.toDto() }
    }

    @PostMapping
    suspend fun addFollowerToFeedback(
        @RequestBody creationDto: FollowerCreateDto,
        @AuthenticationPrincipal user: User
    ): FollowerData {
        val dto = FollowerDto(
            user = user,
            followerType = creationDto.followerType,
            feedbackId = creationDto.feedbackId
        )
        return followerService.addFollowerToFeedback(dto).toDto()
    }

    @PostMapping("/remove")
    suspend fun removeFollowerFromFeedback(
        @RequestBody follow: FollowerCreateDto,
        @AuthenticationPrincipal user: User
    ) {
        val dto = FollowerDto(
            user = user,
            followerType = follow.followerType,
            feedbackId = follow.feedbackId
        )
        followerService.removeFollowerFromFeedback(dto)
    }
}

private fun FeedbackFollower.toDto(): FollowerData {
    return FollowerData(feedbackId, userId, followerType, id)
}
