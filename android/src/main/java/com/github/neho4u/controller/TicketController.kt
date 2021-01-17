package com.github.neho4u.controller

import android.content.SharedPreferences
import com.google.gson.Gson
import com.github.neho4u.model.AuthModel
import com.github.neho4u.model.FullNote
import com.github.neho4u.model.Ticket
import java.net.SocketTimeoutException


class TicketController(private var prefs: SharedPreferences, private var ticketInterface: TicketInterface) {

    var mTickets: ArrayList<Ticket> = ArrayList()

    private fun genericRefreshTickets(partialUrl: String) {
        val prefix = if (prefs.getBoolean(AuthModel.PREF_USE_SSL, true)) "https://" else "http://"
        val cookie = prefs.getString(AuthModel.PREF_COOKIE, "")
        val s = partialUrl +
                prefs.getString(AuthModel.PREF_SESSION_KEY, "")
        FuelManager.instance.basePath = prefix + prefs.getString(AuthModel.PREF_SERVER, "")
        FuelManager.instance.timeoutReadInMillisecond = 30000

        val response = s.httpGet()
                .header("Cookie" to cookie)
                .responseString()
        val result = response.third.component1()
        if (result == null) {
            val error = response.third.component2()
            when (error?.exception) {
                is SocketTimeoutException -> ticketInterface.ticketError("Timeout connecting to server")
                else -> ticketInterface.ticketError(error.toString())
            }
            return
        }

        mTickets = ArrayList()
        val gson = Gson()
        val at: Array<Ticket> = gson.fromJson(result, Array<Ticket>::class.java)
        for (t in at) {
            mTickets.add(t)
        }
        ticketInterface.ticketRefreshResult(mTickets)
    }


    fun refreshMyTickets() {
        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/mine?page=1&limit=500&sessionKey="
        genericRefreshTickets(partUrl)

    }

    fun refreshGroupTickets() {
        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/group?page=1&limit=500&sessionKey="

        genericRefreshTickets(partUrl)
    }

    fun refreshFlaggedTickets() {
        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/flagged?page=1&limit=500&sessionKey="

        genericRefreshTickets(partUrl)
    }

    fun refreshRecentTickets() {
        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/recent?page=1&limit=500&sessionKey="

        genericRefreshTickets(partUrl)
    }

    fun loadFullTicket(ticketId: Int) {

        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/$ticketId?page=1&limit=500&sessionKey="
        val prefix = if (prefs.getBoolean(AuthModel.PREF_USE_SSL, true)) "https://" else "http://"
        val cookie = prefs.getString(AuthModel.PREF_COOKIE, "")
        val s = partUrl +
                prefs.getString(AuthModel.PREF_SESSION_KEY, "")
        FuelManager.instance.basePath = prefix + prefs.getString(AuthModel.PREF_SERVER, "")
        FuelManager.instance.timeoutReadInMillisecond = 30000

        val response = s.httpGet()
                .header("Cookie" to cookie)
                .responseString()
        val result = response.third.component1()
        if (result == null) {
            val error = response.third.component2()
            when (error?.exception) {
                is SocketTimeoutException -> ticketInterface.ticketError("Timeout connecting to server")
                else -> ticketInterface.ticketError(error.toString())
            }
            return
        }
        val t: Ticket = Gson().fromJson(result, Ticket::class.java)

        ticketInterface.ticketLoadResult(t)
    }

    fun sendNote(noteInterface: NoteInterface, note: FullNote) {

        val partUrl = "/helpdesk/WebObjects/Helpdesk.woa/ra/TechNotes?sessionKey="
        val prefix = if (prefs.getBoolean(AuthModel.PREF_USE_SSL, true)) "https://" else "http://"
        val cookie = prefs.getString(AuthModel.PREF_COOKIE, "")
        val s = partUrl +
                prefs.getString(AuthModel.PREF_SESSION_KEY, "")
        FuelManager.instance.basePath = prefix + prefs.getString(AuthModel.PREF_SERVER, "")
        FuelManager.instance.timeoutReadInMillisecond = 30000
        FuelManager.instance.timeoutInMillisecond = 30000

        val body = Gson().toJson(note)

        val response = s.httpPost()
                .header("Cookie" to cookie)
                .body(body)
                .responseString()
        val result = response.second.statusCode
        if (result != 201) {
            val error = response.third.component2().toString()
            noteInterface.noteSendError(error)
            return
        }
        noteInterface.noteSendResult()
    }

    fun getSubCategories(categoryId: Int): Array<String> {
        if (categoryId == 1) {
            return arrayOf("4", "5", "6")
        } else {
            return arrayOf("1", "2", "3")
        }
    }

}