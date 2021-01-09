package com.github.neho4y.comment.service.impl

import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.comment.domain.repository.CommentRepository
import com.github.neho4y.comment.model.CommentCreationDto
import com.github.neho4y.comment.service.CommentService
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val feedbackRepository: FeedbackRepository
) : CommentService {
    override suspend fun addComment(commentCreationDto: CommentCreationDto): Long {
        val feedback = feedbackRepository.findById(commentCreationDto.feedbackId)
            .orElseThrow { NotFoundException("Requested comment not found") }
        val comment = Comment(
            authorId = commentCreationDto.authorId,
            feedbackId = feedback,
            isDeleted = false,
            messageDate = LocalDateTime.now(),
            isUnread = true,
            messageText = commentCreationDto.messageText,
            messageType = commentCreationDto.messageType
        )
        return commentRepository.save(comment).id
    }

    override suspend fun getComments(feedbackId: Long): List<Comment> {
        return commentRepository.findByFeedbackIdAndIsDeletedFalse(
            feedbackRepository.findById(feedbackId).orElseThrow { NotFoundException("Requested comment not found") }
        )
    }

    override suspend fun markRead(commentId: Long) {
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException("Requested comment not found") }
        comment.isUnread = false
        commentRepository.save(comment)
    }

    override suspend fun markDeleted(commentId: Long) {
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException("Requested comment not found") }
        comment.isDeleted = true
        commentRepository.save(comment)
    }
}
