package com.github.neho4u.controller

import com.github.neho4u.model.Note
import com.github.neho4u.model.Ticket
import com.github.neho4u.model.toTicket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackFilter
import com.github.neho4u.utils.Client
import io.ktor.client.features.*

class TicketController(
    private var ticketInterface: TicketInterface
) {
    private suspend fun handleErrors(action: suspend () -> Unit) {
        try {
            action()
        } catch (t: Throwable) {
            when (t) {
                is ResponseException -> ticketInterface.ticketError("Server error :${t.response.status}")
                else -> ticketInterface.ticketError(t.toString())
            }
        }
    }

    private suspend fun genericRefreshTickets(fetchFeedbacks: suspend () -> List<FeedbackDto>) {
        handleErrors { ticketInterface.ticketRefreshResult(fetchFeedbacks().map { it.toTicket() }) }
    }

    suspend fun refreshMyTickets() {
        genericRefreshTickets {
            Client().feedback().getFeed()
        }
    }

    suspend fun refreshAllTickets() {
        genericRefreshTickets {
            Client().feedback().getFilter(FeedbackFilter())
        }
    }

    suspend fun loadFullTicket(ticketId: Long): Ticket {
        val ticket = Client().use { it.feedback().get(ticketId) }.toTicket()
        val (body, comments) = getFeedbackComments(ticketId).partition { it.messageType == "body" }

        return ticket.copy(
            detail = body.firstOrNull()?.messageText,
            notes = comments.map {
                Note(
                    id = it.id,
                    type = it.messageType,
                    mobileNoteText = it.messageText,
                    prettyUpdatedString = "${it.creationDate} by ${it.authorData.username}"
                )
            }
        )
    }

    suspend fun sendComment(commentDto: CommentCreationDto) {
        handleErrors { Client().use { it.comment().add(commentDto) } }
    }

    suspend fun getFeedbackComments(feedbackId: Long): List<CommentDto> {
        return Client().use { it.comment().getForFeedback(feedbackId) }
    }
}
