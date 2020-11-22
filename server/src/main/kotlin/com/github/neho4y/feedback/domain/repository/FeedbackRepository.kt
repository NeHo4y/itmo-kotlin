package com.github.neho4y.feedback.domain.repository

import com.github.neho4y.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<Feedback, Long>
