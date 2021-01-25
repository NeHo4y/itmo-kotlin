package com.github.neho4y.comment.service

import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto

interface CommentService {
    suspend fun addComment(userId: Long, commentCreationDto: CommentCreationDto): Long
    suspend fun getComments(feedbackId: Long): List<CommentDto>
    suspend fun updateComment(commentId: Long, newText: String)
    suspend fun markRead(commentId: Long)
    suspend fun markDeleted(commentId: Long)
}
