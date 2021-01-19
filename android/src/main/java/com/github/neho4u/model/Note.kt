package com.github.neho4u.model

data class Note(
    val id: Long,
    val type: String? = null,
    val prettyUpdatedString: String? = null,
    val mobileNoteText: String? = null,
)
