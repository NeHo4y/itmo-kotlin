package com.github.neho4u.controller

import com.github.neho4u.model.FeedbackFilter
import com.github.neho4u.model.Ticket
import com.github.neho4u.model.toNote
import com.github.neho4u.model.toTicket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerFilterDto
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

    suspend fun refreshMyTickets(filter: FeedbackFilter) {
        genericRefreshTickets {
            Client().use { it.feedback().getFeed(filter.toDto()) }
        }
    }

    suspend fun refreshAllTickets(filter: FeedbackFilter) {
        genericRefreshTickets {
            Client().use { it.feedback().getFilter(filter.toDto()) }
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
                val assignee = client.follower()
                    .getFilter(FollowerFilterDto(ticketId, followerType = FeedbackFollowerType.ASSIGNEE))
                    .firstOrNull()
                t.copy(
                    detail = body.firstOrNull()?.messageText,
                    notes = comments.map { it.toNote() },
                    assignee = assignee?.user?.username
                )
            }
        }
    }

    suspend fun sendComment(commentDto: CommentCreationDto) {
        handleErrors { Client().use { it.comment().add(commentDto) } }
    }
}
