package com.github.neho4u.controller

import android.content.Context
import com.github.neho4u.R
import com.github.neho4u.model.FeedbackFilter
import com.github.neho4u.model.Ticket
import com.github.neho4u.model.toNote
import com.github.neho4u.model.toTicket
import com.github.neho4u.shared.model.feedback.FeedbackDto
import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.feedback.FeedbackStatus
import com.github.neho4u.shared.model.follower.FeedbackFollowerType
import com.github.neho4u.shared.model.follower.FollowerCreateDto
import com.github.neho4u.shared.model.follower.FollowerFilterDto
import com.github.neho4u.utils.Client
import io.ktor.client.features.*

class TicketController(
    private val context: Context?,
    private var ticketInterface: TicketInterface
) {
    private suspend fun <T> handleErrors(action: suspend () -> T): T? {
        return try {
            action()
        } catch (t: Throwable) {
            when (t) {
                is ResponseException ->
                    ticketInterface.ticketError(context?.getString(R.string.error_conn) ?: "EГГОГ")
                is HttpRequestTimeoutException ->
                    ticketInterface.ticketError(context?.getString(R.string.error_timeout) ?: "EГГОГ")
                else -> ticketInterface.ticketError(context?.getString(R.string.error_unknown) ?: "EГГОГ")
            }
            null
        }
    }

    private suspend fun genericRefreshTickets(fetchFeedbacks: suspend () -> List<FeedbackDto>) {
        handleErrors { ticketInterface.ticketRefreshResult(fetchFeedbacks().map { it.toTicket() }) }
    }

    suspend fun refreshMyTickets(filter: FeedbackFilter) {
        genericRefreshTickets { Client().use { it.feedback().getFeed(filter.toDto()) } }
    }

    suspend fun updateTicketStatus(feedbackId: Long, status: FeedbackStatus) {
        handleErrors {
            Client().use {
                it.feedback().updateStatus(feedbackId, status)
            }
        }
    }

    suspend fun updateTicketPriority(feedbackId: Long, priority: FeedbackPriority) {
        handleErrors {
            Client().use {
                it.feedback().updatePriority(feedbackId, priority)
            }
        }
    }

    suspend fun assignOnMe(feedbackId: Long) {
        handleErrors {
            Client().use {
                it.follower().add(FollowerCreateDto(feedbackId, FeedbackFollowerType.ASSIGNEE))
            }
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
}
