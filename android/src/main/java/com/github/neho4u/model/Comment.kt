package com.github.neho4u.model

import com.github.neho4u.shared.model.comment.CommentDto

internal fun CommentDto.toNote(): Note = Note(
    id = id,
    type = messageType,
    mobileNoteText = messageText,
    prettyUpdatedString = "$creationDate by ${authorData.username}"
)
