package com.github.neho4u.shared.model.feedback

import com.github.neho4u.shared.model.user.UserData

data class FeedbackCreationDto(
    val header: String,
    val categoryId: Long,
    val topicId: Long,
    val subtopicId: Long,
    val comment: String
)

data class FeedbackDto(
    var id: Long,
    val header: String?,
    val categoryId: Long?,
    val topicId: Long?,
    val subtopicId: Long?,
    val status: FeedbackStatus?,
    val priority: FeedbackPriority?,
    val authorData: UserData
)
