package com.github.neho4u.model

import com.github.neho4u.shared.model.user.UserData
import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long,
    val type: String? = null,
    val mobileNoteText: String? = null,
    val creationDate: LocalDateTime? = null,
    val userData: UserData? = null
)
