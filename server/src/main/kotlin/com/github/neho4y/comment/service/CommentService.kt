package com.github.neho4y.comment.service

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4y.comment.domain.Comment

interface CommentService {
    suspend fun addComment(commentCreationDto: CommentCreationDto): Long
    suspend fun getComments(feedbackId: Long): List<Comment>
    suspend fun markRead(commentId: Long)
    suspend fun markDeleted(commentId: Long)
}
