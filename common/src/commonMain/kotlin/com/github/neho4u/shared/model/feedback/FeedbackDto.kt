package com.github.neho4u.shared.model.feedback

import com.github.neho4u.shared.model.LocalDateTimeSerializer
import com.github.neho4u.shared.model.common.IdName
import com.github.neho4u.shared.model.user.UserData
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackCreationDto(
    val header: String,
    val categoryId: Long,
    val topicId: Long,
    val subtopicId: Long,
    val comment: String
)

@Serializable
data class FeedbackDto(
    var id: Long,
    val header: String?,
    val category: IdName?,
    val topic: IdName?,
    val subtopic: IdName?,
    val status: FeedbackStatus?,
    val priority: FeedbackPriority?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationDate: LocalDateTime?,
    val authorData: UserData
)
