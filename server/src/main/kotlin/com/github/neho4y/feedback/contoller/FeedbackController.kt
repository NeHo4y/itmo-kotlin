package com.github.neho4y.feedback.contoller

import com.github.neho4u.shared.model.feedback.*
import com.github.neho4y.feedback.service.FeedbackService
import com.github.neho4y.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feedback")
class FeedbackController(private val feedbackService: FeedbackService) {

    @PostMapping("/filter")
    suspend fun getFeedbackByFilter(@RequestBody feedbackFilter: FeedbackFilter): List<FeedbackDto> {
        return feedbackService.getFeedbackByFilter(feedbackFilter)
    }

    @GetMapping("/{id}")
    suspend fun getFeedback(@PathVariable id: Long): FeedbackDto? {
        return feedbackService.getFeedback(id)
    }

    @PostMapping
    suspend fun createFeedback(
        @AuthenticationPrincipal user: User,
        @RequestBody feedbackCreationDto: FeedbackCreationDto
    ): FeedbackDto {
        return feedbackService.createFeedback(user.id, feedbackCreationDto)
    }

    @PostMapping("/{id}")
    suspend fun updateFeedback(@PathVariable id: Long, @RequestBody feedbackDto: FeedbackDto): FeedbackDto {
        return feedbackService.updateFeedback(feedbackDto, id)
    }

    @PostMapping("/{id}/status")
    suspend fun updateFeedback(@PathVariable id: Long, @RequestBody feedbackStatus: FeedbackStatus) {
        return feedbackService.updateStatus(feedbackStatus, id)
    }

    @PostMapping("/{id}/priority")
    suspend fun updateFeedback(@PathVariable id: Long, @RequestBody feedbackPriority: FeedbackPriority) {
        return feedbackService.updatePriority(feedbackPriority, id)
    }

    @PostMapping("/feed")
    suspend fun getAllUserFeedbacks(@AuthenticationPrincipal userId: Long): List<FeedbackDto> {
        return feedbackService.getFeedbacksByFollower(userId)
    }
}
