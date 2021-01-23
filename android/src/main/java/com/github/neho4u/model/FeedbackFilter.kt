package com.github.neho4u.model

import com.github.neho4u.shared.model.feedback.FeedbackFilter as FilterDto

data class FeedbackFilter(
    val number: String? = null,
    val header: String? = null,
    val authorId: Long? = null,
    val category: IdWithName? = null,
    val topic: IdWithName? = null,
    val subtopic: IdWithName? = null,
) {
    fun toDto() = FilterDto(
        number, header, authorId, category?.id, topic?.id, subtopic?.id
    )
}
