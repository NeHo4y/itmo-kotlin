package com.github.neho4y.comment.service

import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.comment.model.CommentCreationDto


interface CommentService {
    fun addComment(commentCreationDto: CommentCreationDto): Long
    fun getComments(feedbackId: Long): List<Comment>
    fun markRead(commentId: Long)
    fun markDeleted(commentId: Long)
}