package com.github.neho4y.comment.controller

import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4y.comment.domain.Comment

internal fun Comment.toCommentDto() = CommentDto(
    authorId = authorId,
    messageType = messageType,
    messageText = messageText
)
