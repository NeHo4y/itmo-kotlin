package com.github.neho4y.comment.domain.repository

import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByFeedbackIdAndIsDeletedFalse(feedbackId: Feedback): List<Comment>
}