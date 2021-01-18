package com.github.neho4u.shared.model.feedback

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackFilter(
    val number: String? = null,
    val header: String? = null,
    val authorId: Long? = null
)
