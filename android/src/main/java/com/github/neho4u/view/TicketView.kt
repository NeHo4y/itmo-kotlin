package com.github.neho4u.view

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.neho4u.R
import com.github.neho4u.controller.NoteInterface
import com.github.neho4u.controller.TicketController
import com.github.neho4u.controller.TicketInterface
import com.github.neho4u.databinding.ATicketViewBinding
import com.github.neho4u.databinding.DialogNoteLayoutBinding
import com.github.neho4u.databinding.NoteDetailBinding
import com.github.neho4u.model.Ticket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TicketView : AppCompatActivity(), TicketInterface, NoteInterface {

    private lateinit var ticketController: TicketController
    private var ticketId: Long = -1L
    private var ticket: Ticket? = null
    private var menuRefresh: MenuItem? = null
    private var menuNewNote: MenuItem? = null
    private lateinit var markdown: Markwon
    private lateinit var noteBinding: ATicketViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteBinding = DataBindingUtil.setContentView(this, R.layout.a_ticket_view)

        ticketId = intent.getLongExtra(ARG_TICKET_ID, -1L)
        ticketController = TicketController(this)

        setSupportActionBar(noteBinding.ticketViewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        markdown = Markwon.create(applicationContext)

        startRefresh()
    }

    private fun startRefresh() {
        noteBinding.pbTicketView.visibility = View.VISIBLE
        menuRefresh?.apply {
            isVisible = false
        }
        GlobalScope.launch(Dispatchers.Default) {
            ticketController.loadFullTicket(ticketId)?.let {
                ticketLoadResult(it)
            }
        }
    }

    private fun inflateTicket(ticket: Ticket) {
        title = "Ticket " + ticket.id
        noteBinding.tvTicketViewId.visibility = View.VISIBLE
        noteBinding.tvTicketViewId.text = ticket.id.toString()

        if (ticket.displayClient != null) {
            noteBinding.tvTicketViewClientFullname.visibility = View.VISIBLE
            noteBinding.tvTicketViewClientFullname.text = ticket.displayClient
        }

        if (ticket.lastUpdated != null) {
            noteBinding.tvTicketViewLastUpdated.visibility = View.VISIBLE
            noteBinding.tvTicketViewLastUpdated.text = getString(R.string.last_updated, ticket.lastUpdated.toString())
        }

        if (ticket.priority != null) {
            noteBinding.tvSeverity.visibility = View.VISIBLE
            noteBinding.tvSeverity.text = getString(R.string.severity_text, ticket.priority)
        }

        if (ticket.subject != null) {
            noteBinding.tvTicketViewSubject.visibility = View.VISIBLE
            noteBinding.tvTicketViewSubject.text = ticket.subject
        }

        if (ticket.assignee != null) {
            with(noteBinding.tvTicketViewAssignedTech) {
                visibility = View.VISIBLE
                text = ticket.assignee
            }
        }

        noteBinding.vTicketViewLine2.visibility = View.VISIBLE

        if (ticket.detail != null) {
            noteBinding.tvTicketViewDetails.visibility = View.VISIBLE
            markdown.setMarkdown(noteBinding.tvTicketViewDetails, ticket.detail)
        }

        if (ticket.notes != null) {
            noteBinding.llNoteContent.removeAllViews()
            for (i in ticket.notes.indices) {
                val note = ticket.notes[i]
                val noteView = LayoutInflater.from(this)
                    .inflate(R.layout.note_detail, noteBinding.ticketViewParent, false)

                NoteDetailBinding.bind(noteView).apply {
                    tvNoteDetailResolution.visibility = View.GONE
                    tvNoteDetailPrettyLastUpdated.text =
                        getString(R.string.pretty_updated_string, note.creationDate, note.userData?.username)
                    tvNoteDetailDetail.text = note.mobileNoteText
                }

                noteBinding.llNoteContent.addView(noteView)
                Log.d("TicketView", "Added note view for note id " + note.id)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_dv_refresh -> {
                startRefresh()
                true
            }
            R.id.menu_add_note -> {
                val dialog = Dialog(this)
                val dialogNoteLayoutBinding: DialogNoteLayoutBinding =
                    DialogNoteLayoutBinding.inflate(LayoutInflater.from(this))

                dialog.setContentView(dialogNoteLayoutBinding.root)

                dialog.setTitle(resources.getString(R.string.new_note))

                dialogNoteLayoutBinding.bNoteCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialogNoteLayoutBinding.bNoteOk.setOnClickListener {
                    val note = CommentCreationDto(
                        ticketId,
                        "message",
                        dialogNoteLayoutBinding.etNoteText.text.toString()

                    )
                    Log.d("TicketView", "Submitting note $note")
                    noteBinding.pbTicketView.visibility = View.VISIBLE
                    menuRefresh?.apply {
                        isVisible = false
                    }
                    GlobalScope.launch(Dispatchers.Default) {
                        ticketController.sendComment(note)
                        noteSendResult()
                    }
                    dialog.dismiss()
                }

                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_view, menu)
        menuInflater.inflate(R.menu.menu_ticket_view, menu)
        menuRefresh = menu?.findItem(R.id.menu_dv_refresh)
        menuNewNote = menu?.findItem(R.id.menu_add_note)
        return true
    }

    override fun ticketRefreshResult(result: List<Ticket>) {
        // nothing here
    }

    override fun ticketLoadResult(result: Ticket) {
        Log.d("TicketView", "Got ticket result: $result")
        this.ticket = result
        this.runOnUiThread {
            menuRefresh?.apply {
                isVisible = true
            }
            menuNewNote?.apply {
                isVisible = true
            }
            noteBinding.pbTicketView.visibility = View.GONE
            inflateTicket(result)
        }
    }

    override fun noteSendResult() {
        this.runOnUiThread {
            Log.d("TicketView", "Got note result OK")
            this.startRefresh()
        }
    }

    override fun ticketError(error: String) {
        this.runOnUiThread {
            Log.d("TicketView", "Failed to load ticket: $error")
            showError("Failed to load ticket: $error")
        }
    }

    override fun noteSendError(error: String) {
        this.runOnUiThread {
            Log.d("TicketView", "Failed to send note: $error")
            showError("Failed to add note: $error")
        }
    }

    private fun showError(error: String) {
        menuRefresh?.apply {
            isVisible = true
        }
        noteBinding.pbTicketView.visibility = View.GONE
        Snackbar.make(noteBinding.ticketViewParent, error, Snackbar.LENGTH_INDEFINITE).apply {
            view.findViewById<TextView>(R.id.snackbar_text).apply {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
            }
        }.show()
    }

    companion object {
        const val ARG_TICKET_ID = "ticket_id"
    }
}
