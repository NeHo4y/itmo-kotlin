package com.github.neho4y.feedback.domain.repository

import com.github.neho4y.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface FeedbackSpecificationRepository :
    JpaRepository<Feedback, Long>,
    JpaSpecificationExecutor<Feedback>
