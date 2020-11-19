// ktlint-disable filename
package com.github.neho4y.comment.model

class CommentCreationDto(
    val feedbackId: Long,
    val authorId: Long,
    val messageType: String,
    val messageText: String
)
