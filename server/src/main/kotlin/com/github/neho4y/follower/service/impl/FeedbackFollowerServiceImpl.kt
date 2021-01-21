package com.github.neho4y.follower.service.impl

import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerData
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4y.common.exception.NotAllowedException
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.domain.repository.FeedbackFollowerSpecificationRepository
import com.github.neho4y.follower.domain.repository.FollowerSearchSpecification
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.service.FeedbackFollowerService
import com.github.neho4y.integration.camunda.service.CamundaService
import com.github.neho4y.user.domain.User
import com.github.neho4y.user.model.toUserData
import com.github.neho4y.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FeedbackFollowerServiceImpl(
    private val followerRepository: FeedbackFollowerSpecificationRepository,
    private val userService: UserService,
    private val camundaService: CamundaService
) : FeedbackFollowerService {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override suspend fun findFollowsByFilter(filter: FollowerFilterDto): List<FollowerData> {
        return followerRepository.findAll(FollowerSearchSpecification(filter))
            .mapNotNull { f -> f.toDto() }
    }

    override suspend fun addFollowerToFeedback(creationDto: FollowerDto): FollowerData {
        val filter = FollowerFilterDto(creationDto.feedbackId, creationDto.user.id, creationDto.followerType)
        val existed = followerRepository.findOne(FollowerSearchSpecification(filter))
        if (existed.isPresent) {
            log.info("Try to add already existed follower")
            return existed.get().toDto()
        }
        checkFollowerConstraints(creationDto)
        val follower = followerRepository.save(
            FeedbackFollower(creationDto.feedbackId, creationDto.user.id, creationDto.followerType)
        )
        if (creationDto.followerType == FeedbackFollowerType.ASSIGNEE) {
            fireFeedbackAssign(creationDto.feedbackId, creationDto.user)
        }
        log.info("New feedback follower: $follower")
        return follower.toDto()
    }

    private suspend fun fireFeedbackAssign(feedbackId: Long, user: User) {
        if (camundaService.isEnabled()) {
            camundaService.assignFeedback(feedbackId, user)
        }
    }

    override suspend fun removeFollowerFromFeedback(follow: FollowerDto) {
        val existed = findFollowsByFilter(FollowerFilterDto(follow.feedbackId, follow.user.id, follow.followerType))
            .firstOrNull() ?: throw NotFoundException("User is not a follower")
        followerRepository.deleteById(existed.id)
        log.info("Feedback follower deleted: $existed")
    }

    private suspend fun checkFollowerConstraints(creationDto: FollowerDto) {
        val user = creationDto.user
        val type = creationDto.followerType
        if (user.role == UserRole.USER && type == FeedbackFollowerType.ASSIGNEE) {
            throw NotAllowedException("User ${user.id} cannot be an assignee")
        }
    }

    private suspend fun getUserData(userId: Long): UserData {
        return userService.findById(userId).toUserData()
    }

    private suspend fun FeedbackFollower.toDto(): FollowerData {
        return FollowerData(feedbackId, getUserData(userId), followerType, id)
    }
}
