package com.github.neho4u.model

import kotlinx.datetime.LocalDateTime

data class Ticket(
    val id: Long,
    val lastUpdated: LocalDateTime? = null,
    val subject: String? = null,
    val detail: String? = null,
    val displayClient: String? = null,
    val priority: String? = null,
    val notes: List<Note>? = null,
    val assignee: String? = null,
    val status: String? = null
)
