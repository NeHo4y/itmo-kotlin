package com.github.neho4u.controller

import com.github.neho4u.model.Ticket

interface TicketInterface {
    fun ticketRefreshResult(result: List<Ticket>)
    fun ticketError(error: String)
    fun ticketLoadResult(result: Ticket)
}
