package com.github.neho4y.feedback.model

import com.github.neho4y.feedback.domain.FeedbackPriority
import com.github.neho4y.feedback.domain.FeedbackStatus

data class FeedbackCreationDto(

    val header: String,
    val categoryId: Long,
    val topicId: Long,
    val subtopicId: Long,
    val comment: String,

)

data class FeedbackDto(

    val id: Long,
    val header: String?,
    val categoryId: Long?,
    val topicId: Long?,
    val subtopicId: Long?,
    val status: FeedbackStatus?,
    val priority: FeedbackPriority?

)
