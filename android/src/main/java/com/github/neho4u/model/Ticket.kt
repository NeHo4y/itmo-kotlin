package com.github.neho4u.model

import kotlinx.datetime.LocalDateTime

data class Ticket(
    val id: Long,
    val lastUpdated: LocalDateTime? = null,
    val subject: String? = null,
    val shortSubject: String? = null,
    val detail: String? = null,
    val shortDetail: String? = null,
    val displayClient: String? = null,
    val priority: String? = null,
    val notes: List<Note>? = null,
)
