// ktlint-disable filename
package com.github.neho4y.comment.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentCreationDto(
    val feedbackId: Long,
    val authorId: Long,
    val messageType: String,
    val messageText: String
)

@Serializable
data class CommentDto(
    val authorId: Long,
    val messageType: String,
    val messageText: String
)
