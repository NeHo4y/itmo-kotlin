package com.github.neho4y.follower.service.impl

import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.domain.repository.FeedbackFollowerSpecificationRepository
import com.github.neho4y.follower.domain.repository.FollowerSearchSpecification
import com.github.neho4y.follower.model.FollowerDto
import com.github.neho4y.follower.model.FollowerFilterDto
import com.github.neho4y.follower.service.FeedbackFollowerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FeedbackFollowerServiceImpl(
    private val followerRepository: FeedbackFollowerSpecificationRepository
) : FeedbackFollowerService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun findFollowsByFilter(filter: FollowerFilterDto): List<FeedbackFollower> {
        return followerRepository.findAll(FollowerSearchSpecification(filter))
    }

    override fun addFollowerToFeedback(creationDto: FollowerDto): FeedbackFollower {
        val filter = FollowerFilterDto(creationDto.feedback.id, creationDto.user.id, creationDto.followerType)
        val existed = followerRepository.findOne(FollowerSearchSpecification(filter))
        if (existed.isPresent) {
            log.info("Try to add already existed follower")
            return existed.get()
        }
        val follower = followerRepository.save(
            FeedbackFollower(creationDto.feedback.id, creationDto.user.id, creationDto.followerType)
        )
        log.info("New feedback follower: $follower")
        return follower
    }

    override fun removeFollowerFromFeedback(followId: Long) {
        val existed = followerRepository.findById(followId)
            .orElseThrow { NotFoundException("User is not a follower") }
        followerRepository.deleteById(followId)
        log.info("Feedback follower deleted: $existed")
    }
}
