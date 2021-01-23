package com.github.neho4y.feedback.service.impl

import com.github.neho4u.shared.model.common.IdName
import com.github.neho4u.shared.model.feedback.*
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.category.service.SubtopicService
import com.github.neho4y.category.service.TopicService
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.feedback.domain.Feedback
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.feedback.domain.repository.FeedbackSearchSpecification
import com.github.neho4y.feedback.domain.repository.FeedbackSpecificationRepository
import com.github.neho4y.feedback.service.FeedbackService
import com.github.neho4y.follower.service.FeedbackFollowerService
import com.github.neho4y.user.model.toUserData
import com.github.neho4y.user.service.UserService
import kotlinx.datetime.toKotlinLocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FeedbackServiceImpl(
    private val feedbackSpecificationRepository: FeedbackSpecificationRepository,
    private val feedbackRepository: FeedbackRepository,
    private val userService: UserService,
    private val followerService: FeedbackFollowerService,
    private val categoryService: CategoryService,
    private val topicService: TopicService,
    private val subtopicService: SubtopicService
) : FeedbackService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun getFeedbackByFilter(feedbackFilter: FeedbackFilter): List<FeedbackDto> {
        return feedbackSpecificationRepository.findAll(FeedbackSearchSpecification(feedbackFilter))
            .map { convertToDto(it) }
    }

    override suspend fun getFeedbacksByFollower(userId: Long, filter: FeedbackFilter): List<FeedbackDto> {
        val followerFilter = FollowerFilterDto(null, userId, null)
        val followers = followerService.findFollowsByFilter(followerFilter)
        val myFeedbacksFilter = filter.copy(authorId = userId)
        val myFeedbacks = feedbackSpecificationRepository.findAll(FeedbackSearchSpecification(myFeedbacksFilter))
        val followed = followers.map {
            feedbackRepository.findById(it.feedbackId)
                .orElseThrow { NotFoundException("Unable to find feedback") }
        }
        return myFeedbacks.union(followed).map { convertToDto(it) }
    }

    override suspend fun createFeedback(userId: Long, feedbackCreationDto: FeedbackCreationDto): FeedbackDto {
        val currentDateTime = LocalDateTime.now()
        val newFeedback = Feedback(
            feedbackCreationDto.header,
            currentDateTime,
            currentDateTime,
            feedbackCreationDto.categoryId,
            feedbackCreationDto.topicId,
            feedbackCreationDto.subtopicId,
            userId
        )
        val savedFeedback = feedbackRepository.save(newFeedback)
        log.info("new feedback with id: ${savedFeedback.id} is saved")
        return convertToDto(savedFeedback)
    }

    override suspend fun getFeedback(id: Long): FeedbackDto {
        return convertToDto(
            feedbackRepository.findById(id)
                .orElseThrow { NotFoundException("Unable to find feedback") }
        )
    }

    override suspend fun updateFeedback(feedbackDto: FeedbackDto, id: Long): FeedbackDto {
        val feedback = feedbackRepository.findById(id)
            .orElseThrow { NotFoundException("Unable to find feedback") }
        val newCategoryId = feedbackDto.category?.id
        val newTopicId = feedbackDto.topic?.id
        val newSubtopicId = feedbackDto.subtopic?.id
        feedback.apply {
            header = feedbackDto.header ?: header
            categoryId = newCategoryId ?: categoryId
            topicId = newTopicId ?: topicId
            subtopicId = newSubtopicId ?: subtopicId
            status = feedbackDto.status ?: status
            priority = feedbackDto.priority ?: priority
            updateDate = LocalDateTime.now()
        }
        val updatedFeedback = feedbackRepository.save(feedback)
        return convertToDto(updatedFeedback)
    }

    override suspend fun updateStatus(status: FeedbackStatus, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.status = status
        if (FeedbackStatus.CLOSED == status || FeedbackStatus.RESOLVED == status) {
            feedback.endDate = LocalDateTime.now()
        }
        feedbackRepository.save(feedback)
    }

    override suspend fun updatePriority(priority: FeedbackPriority, id: Long) {
        val feedback = feedbackRepository.findById(id).orElseThrow { NotFoundException("Unable to find feedback") }
        feedback.priority = priority
        feedbackRepository.save(feedback)
    }

    suspend fun convertToDto(feedback: Feedback): FeedbackDto {
        val userData = userService.findById(feedback.authorId).toUserData()
        val categoryDto = categoryService.getCategory(feedback.categoryId)
        val topicDto = topicService.getTopic(feedback.topicId)
        val subtopicDto = subtopicService.getSubtopic(feedback.subtopicId)
        return FeedbackDto(
            id = feedback.id,
            header = feedback.header,
            category = IdName(categoryDto.id, categoryDto.description),
            topic = IdName(topicDto.id, topicDto.description),
            subtopic = IdName(subtopicDto.id, subtopicDto.description),
            status = feedback.status,
            priority = feedback.priority,
            authorData = userData,
            creationDate = feedback.creationDate.toKotlinLocalDateTime()
        )
    }
}
