package com.github.neho4u.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.neho4u.R
import com.github.neho4u.controller.CommentController
import com.github.neho4u.controller.NoteInterface
import com.github.neho4u.controller.TicketController
import com.github.neho4u.controller.TicketInterface
import com.github.neho4u.databinding.ATicketViewBinding
import com.github.neho4u.databinding.DialogNoteLayoutBinding
import com.github.neho4u.databinding.NoteDetailBinding
import com.github.neho4u.model.Ticket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4u.utils.AndroidTokenProvider
import com.google.android.material.snackbar.Snackbar
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TicketView : AppCompatActivity(), TicketInterface, NoteInterface {

    private lateinit var ticketController: TicketController
    private lateinit var commentController: CommentController
    private var ticketId: Long = -1L
    private var ticket: Ticket? = null
    private var menuNewNote: MenuItem? = null
    private lateinit var markdown: Markwon
    private lateinit var noteBinding: ATicketViewBinding
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private var menuAssign: MenuItem? = null
    private var menuChangeStatus: MenuItem? = null
    private var currentUser: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteBinding = DataBindingUtil.setContentView(this, R.layout.a_ticket_view)
        ticketId = intent.getLongExtra(ARG_TICKET_ID, -1L)
        ticketController = TicketController(this, this)
        commentController = CommentController(this, this)
        currentUser = AndroidTokenProvider.getUserData()

        setSupportActionBar(noteBinding.ticketViewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        markdown = Markwon.create(applicationContext)

        pullToRefresh = noteBinding.ticketPullRefresh.apply {
            setOnRefreshListener {
                startRefresh()
            }
        }

        startRefresh()
    }

    private fun startRefresh() {
        menuNewNote?.isEnabled = false
        menuAssign?.isEnabled = false
        menuChangeStatus?.isEnabled = false
        GlobalScope.launch(Dispatchers.Default) {
            ticketController.loadFullTicket(ticketId)?.let {
                ticketLoadResult(it)
            }
            withContext(Dispatchers.Main) {
                noteBinding.pbTicketView.visibility = View.GONE
                pullToRefresh.isRefreshing = false
            }
        }
    }

    private fun inflateTicket(ticket: Ticket) {
        title = getString(R.string.ticket_title, ticket.id.toString())
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
            noteBinding.tvSeverity.text = getString(R.string.severity_text, ticket.priority.name)
        }

        if (ticket.status != null) {
            noteBinding.tvStatus.visibility = View.VISIBLE
            noteBinding.tvStatus.text = ticket.status.name
        }

        if (ticket.subject != null) {
            noteBinding.tvTicketViewSubject.visibility = View.VISIBLE
            noteBinding.tvTicketViewSubject.text = ticket.subject
        }

        if (ticket.assignee != null) {
            with(noteBinding.tvTicketViewAssignedTech) {
                visibility = View.VISIBLE
                text = getString(R.string.assigned_to, ticket.assignee)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_change_status -> {
                ChangeStatusDialogWrapper(this, ticketId) {
                    startRefresh()
                }.show(ticket?.status, ticket?.priority)
                true
            }
            R.id.menu_assign_to_me -> {
                GlobalScope.launch {
                    ticketController.assignOnMe(ticketId)
                    withContext(Dispatchers.Main) {
                        startRefresh()
                    }
                }
                true
            }
            R.id.menu_add_note -> {
                val dialog = Dialog(this)
                val dialogNoteLayoutBinding = DialogNoteLayoutBinding.inflate(LayoutInflater.from(this))

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
                    GlobalScope.launch(Dispatchers.Default) {
                        commentController.sendComment(note)
                        noteSendResult()
                    }
                    dialog.dismiss()
                }
                dialog.show()
                dialogNoteLayoutBinding.etNoteText.requestFocus()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ticket_view, menu)
        menuNewNote = menu?.findItem(R.id.menu_add_note)
        menuAssign = menu?.findItem(R.id.menu_assign_to_me)?.apply {
            isVisible = currentUser?.role == UserRole.ADMIN
        }
        menuChangeStatus = menu?.findItem(R.id.menu_change_status)?.apply {
            isVisible = currentUser?.role == UserRole.ADMIN
        }
        return true
    }

    override fun ticketRefreshResult(result: List<Ticket>) {
        // nothing here
    }

    override fun ticketLoadResult(result: Ticket) {
        Log.d("TicketView", "Got ticket result: $result")
        this.ticket = result
        this.runOnUiThread {
            menuNewNote?.isEnabled = true
            menuChangeStatus?.isEnabled = true
            menuAssign?.isEnabled = true
            menuAssign?.isVisible = result.assignee.isNullOrBlank() &&
                currentUser?.role == UserRole.ADMIN
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
            showError(getString(R.string.error_ticket_load, error))
        }
    }

    override fun noteSendError(error: String) {
        this.runOnUiThread {
            Log.d("TicketView", "Failed to send note: $error")
            showError(getString(R.string.error_note_load, error))
        }
    }

    private fun showError(error: String) {
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
