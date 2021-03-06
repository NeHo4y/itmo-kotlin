package com.github.neho4u.controller

import com.github.neho4u.model.Ticket
import com.github.neho4u.model.toNote
import com.github.neho4u.model.toTicket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackFilter
import com.github.neho4u.utils.Client
import io.ktor.client.features.*

class TicketController(
    private var ticketInterface: TicketInterface
) {
    private suspend fun <T> handleErrors(action: suspend () -> T): T? {
        return try {
            action()
        } catch (t: Throwable) {
            when (t) {
                is ResponseException -> ticketInterface.ticketError("Server error :${t.response.status}")
                is HttpRequestTimeoutException -> ticketInterface.ticketError("Timeout error")
                else -> ticketInterface.ticketError(t.toString())
            }
            null
        }
    }

    private suspend fun genericRefreshTickets(fetchFeedbacks: suspend () -> List<FeedbackDto>) {
        handleErrors { ticketInterface.ticketRefreshResult(fetchFeedbacks().map { it.toTicket() }) }
    }

    suspend fun refreshMyTickets() {
        genericRefreshTickets {
            Client().use { it.feedback().getFeed() }
        }
    }

    suspend fun refreshAllTickets() {
        genericRefreshTickets {
            Client().use { it.feedback().getFilter(FeedbackFilter()) }
        }
    }

    suspend fun loadFullTicket(ticketId: Long): Ticket? {
        return handleErrors {
            Client().use { client ->
                val t = client.feedback()
                    .get(ticketId)
                    .toTicket()
                val (body, comments) = client.comment()
                    .getForFeedback(t.id)
                    .partition { it.messageType == "body" }
                t.copy(
                    detail = body.firstOrNull()?.messageText,
                    notes = comments.map { it.toNote() }
                )
            }
        }
    }

    suspend fun sendComment(commentDto: CommentCreationDto) {
        handleErrors { Client().use { it.comment().add(commentDto) } }
    }
}
