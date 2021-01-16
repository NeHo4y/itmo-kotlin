package com.github.neho4y.comment.service.impl

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.comment.domain.repository.CommentRepository
import com.github.neho4y.comment.service.CommentService
import com.github.neho4y.common.exception.NotFoundException
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import com.github.neho4y.user.controller.toUserData
import com.github.neho4y.user.service.UserService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val feedbackRepository: FeedbackRepository,
    private val userService: UserService
) : CommentService {
    override suspend fun addComment(userId: Long, commentCreationDto: CommentCreationDto): Long {
        val feedback = feedbackRepository.findById(commentCreationDto.feedbackId)
            .orElseThrow { NotFoundException("Requested comment not found") }
        val comment = Comment(
            authorId = userId,
            feedbackId = feedback,
            isDeleted = false,
            messageDate = LocalDateTime.now(),
            isUnread = true,
            messageText = commentCreationDto.messageText,
            messageType = commentCreationDto.messageType
        )
        return commentRepository.save(comment).id
    }

    override suspend fun getComments(feedbackId: Long): List<CommentDto> {
        return commentRepository.findByFeedbackIdAndIsDeletedFalse(
            feedbackRepository.findById(feedbackId).orElseThrow { NotFoundException("Requested comment not found") }
        ).map { convertToDto(it) }
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

    suspend fun convertToDto(comment: Comment): CommentDto {
        val userData = userService.findById(comment.authorId).toUserData()
        return CommentDto(
            id = comment.id,
            isUnread = comment.isUnread,
            feedbackId = comment.feedbackId.id,
            messageType = comment.messageType,
            messageText = comment.messageText,
            authorData = userData
        )
    }
}
