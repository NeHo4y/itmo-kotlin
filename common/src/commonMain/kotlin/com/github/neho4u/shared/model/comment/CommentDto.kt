// ktlint-disable filename
package com.github.neho4u.shared.model.comment

import com.github.neho4u.shared.model.LocalDateTimeSerializer
import com.github.neho4u.shared.model.user.UserData
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CommentCreationDto(
    val feedbackId: Long,
    val messageType: String,
    val messageText: String
)

@Serializable
data class CommentUpdateDto(
    val messageText: String
)

@Serializable
data class CommentDto(
    val id: Long,
    val isUnread: Boolean,
    val feedbackId: Long,
    val authorData: UserData,
    val messageType: String,
    val messageText: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationDate: LocalDateTime
)
