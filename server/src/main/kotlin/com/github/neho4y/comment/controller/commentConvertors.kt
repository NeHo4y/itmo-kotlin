package com.github.neho4y.comment.controller
import com.github.neho4y.comment.domain.Comment
import com.github.neho4y.comment.model.CommentDto

internal fun Comment.toCommentDto() = CommentDto(
    authorId = authorId,
    messageType = messageType,
    messageText = messageText
)
