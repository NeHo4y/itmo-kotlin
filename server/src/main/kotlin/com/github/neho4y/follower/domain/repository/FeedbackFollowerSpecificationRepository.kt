package com.github.neho4y.follower.domain.repository

import com.github.neho4y.follower.domain.FeedbackFollower
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface FeedbackFollowerSpecificationRepository : JpaRepository<FeedbackFollower, Long>,
    JpaSpecificationExecutor<FeedbackFollower>
