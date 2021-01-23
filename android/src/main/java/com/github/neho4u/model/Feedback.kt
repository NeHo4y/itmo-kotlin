package com.github.neho4u.model

import com.github.neho4u.shared.model.feedback.FeedbackDto

internal fun FeedbackDto.toTicket() = Ticket(
    id = id,
    subject = header,
    displayClient = authorData.username,
    lastUpdated = creationDate,
    detail = "${category?.name} : ${topic?.name} : ${subtopic?.name}",
    priority = priority?.name,
    status = status?.name
)
