package com.github.neho4y.comment.service.impl

import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.comment.domain.repository.CommentRepository
import com.github.neho4y.comment.model.CommentCreationDto
import com.github.neho4y.comment.service.CommentService
import com.github.neho4y.feedback.domain.repository.FeedbackRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val feedbackRepository: FeedbackRepository
) : CommentService {
    override fun addComment(commentCreationDto: CommentCreationDto): Long {
        val feedback = feedbackRepository.findById(commentCreationDto.feedbackId).orElseThrow()
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

    override fun getComments(feedbackId: Long): List<Comment> {
        return commentRepository.findByFeedbackIdAndIsDeletedFalse(
            feedbackRepository.findById(feedbackId).orElseThrow()
        )
    }

    override fun markRead(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow()
        comment.isUnread = false
        commentRepository.save(comment)
    }

    override fun markDeleted(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow()
        comment.isDeleted = true
        commentRepository.save(comment)
    }
    
}