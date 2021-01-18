package com.github.neho4u.model

import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long,
    val type: String? = null,
    val localDateTime: LocalDateTime? = null,
    val isSolution: Boolean? = null,
    val prettyUpdatedString: String? = null,
    val mobileNoteText: String? = null,
    val isTechNote: Boolean? = null,
    val noteColor: String? = null,
    val noteClass: String? = null
)

// TODO long note on click?
// "id": 35,
// "type": "TechNote",
// "date": "2019-02-12T08:25:23Z",
// "isSolution": false,
// "prettyUpdatedString": "2 weeks ago <strong>Joe Admin</strong> said",
// "mobileNoteText": "New deadline: the next Friday.",
// "isTechNote": true,
// "isHidden": false,
// "attachments": []
