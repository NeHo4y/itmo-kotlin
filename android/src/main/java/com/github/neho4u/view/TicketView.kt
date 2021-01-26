package com.github.neho4u.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
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
import com.github.neho4u.databinding.NoteDetailBinding
import com.github.neho4u.model.Ticket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.comment.CommentUpdateDto
import com.github.neho4u.shared.model.feedback.FeedbackUpdateDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4u.utils.*
import com.google.android.material.snackbar.Snackbar
import io.ktor.utils.io.core.*
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.toJavaLocalDateTime

class TicketView : AppCompatActivity(), TicketInterface, NoteInterface, IShowError {

    private lateinit var ticketController: TicketController
    private lateinit var commentController: CommentController
    private var ticketId: Long = -1L
    private var ticket: Ticket? = null
    private var menuNewNote: MenuItem? = null
    private lateinit var markdown: Markwon
    private lateinit var noteBinding: ATicketViewBinding
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var menuAssign: TextView
    private lateinit var menuChangeStatus: ImageButton
    private var currentUser: UserData? = null
    private var menuEdit: MenuItem? = null

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

        menuChangeStatus = noteBinding.bEditProps.apply {
            setOnClickListener {
                ChangeStatusDialogWrapper(this@TicketView, ticketId) {
                    startRefresh()
                }.show(ticket?.status, ticket?.priority)
            }
            visibility = View.GONE
        }

        menuAssign = noteBinding.tvAssignToMe.apply {
            setOnClickListener {
                GlobalScope.launch {
                    ticketController.assignOnMe(ticketId)
                    withContext(Dispatchers.Main) {
                        startRefresh()
                    }
                }
            }
            visibility = View.GONE
        }

        startRefresh()
    }

    private fun startRefresh() {
        menuNewNote?.isEnabled = false
        menuAssign.isEnabled = false
        menuChangeStatus.isEnabled = false
        menuEdit?.isEnabled = false
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
        noteBinding.tvFeedbackProperties.visibility = View.VISIBLE
        noteBinding.tvPriorityStatus.visibility = View.VISIBLE
        noteBinding.tvFeedbackStatus.visibility = View.VISIBLE
        noteBinding.vTicketViewLine3.visibility = View.VISIBLE

        if (ticket.displayClient != null) {
            noteBinding.tvTicketViewClientFullname.visibility = View.VISIBLE
            noteBinding.tvTicketViewClientFullname.text = ticket.displayClient
        }

        if (ticket.lastUpdated != null) {
            noteBinding.tvTicketViewLastUpdated.visibility = View.VISIBLE
            noteBinding.tvTicketViewLastUpdated.text = getString(
                R.string.last_updated,
                ticket.lastUpdated
                    .toJavaLocalDateTime()
                    .atCurrentTimeZone()
                    .format(dateFormatter)
            )
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
                    tvNoteDetailPrettyLastUpdated.text =
                        getString(
                            R.string.pretty_updated_string,
                            note.creationDate
                                ?.toJavaLocalDateTime()
                                ?.atCurrentTimeZone()
                                ?.format(dateFormatter),
                            note.userData?.username
                        )
                    markdown.setMarkdown(tvNoteDetailDetail, note.mobileNoteText ?: "")
                }

                noteView.findViewById<ImageButton>(R.id.image_button)?.apply {
                    if (currentUser?.id != note.userData?.id) {
                        visibility = View.INVISIBLE
                    }
                    setOnClickListener {
                        NoteDialogWrapper(this@TicketView, note) { text, dialog ->
                            val noteEdit = CommentUpdateDto(text)
                            Log.d("TicketView", "Editing note $noteEdit")
                            GlobalScope.launch(Dispatchers.Default) {
                                commentController.updateComment(note.id, noteEdit)
                                withContext(Dispatchers.Main) {
                                    dialog.dismiss()
                                    noteSendResult()
                                }
                            }
                        }.show()
                    }
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
            R.id.menu_add_note -> {
                NoteDialogWrapper(this, null) { text, dialog ->
                    val note = CommentCreationDto(ticketId, "message", text)
                    Log.d("TicketView", "Submitting note $note")
                    GlobalScope.launch(Dispatchers.Default) {
                        commentController.sendComment(note)
                        withContext(Dispatchers.Main) {
                            dialog.dismiss()
                            noteSendResult()
                        }
                    }
                }.show()
                true
            }
            R.id.menu_edit_feedback -> {
                NewTicketDialogWrapper(this, this, ticket) { creationDto, dialog ->
                    val updateDto = FeedbackUpdateDto(
                        header = creationDto.header,
                        categoryId = creationDto.categoryId,
                        topicId = creationDto.topicId,
                        subtopicId = creationDto.subtopicId,
                        comment = creationDto.comment
                    )
                    Log.w("menu_edit_feedback", updateDto.toString())
                    GlobalScope.launch {
                        try {
                            Client().use { it.feedback().update(ticketId, updateDto) }
                        } catch (e: Throwable) {
                            showError(getString(R.string.error_conn))
                        }
                        withContext(Dispatchers.Main) {
                            dialog.dismiss()
                            startRefresh()
                        }
                    }
                }.also {
                    it.show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ticket_view, menu)
        menuNewNote = menu?.findItem(R.id.menu_add_note)
        menuEdit = menu?.findItem(R.id.menu_edit_feedback)?.apply {
            isVisible = currentUser?.id == ticket?.authorData?.id
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
            menuChangeStatus.apply {
                isEnabled = true
                visibility = if (currentUser?.role == UserRole.ADMIN) View.VISIBLE else View.GONE
            }
            menuAssign.apply {
                isEnabled = true
                visibility =
                    if (result.assignee.isNullOrBlank() && currentUser?.role == UserRole.ADMIN) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
            noteBinding.pbTicketView.visibility = View.GONE
            menuEdit?.isVisible = currentUser?.id == ticket?.authorData?.id
            menuEdit?.isEnabled = true
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

    override fun showError(error: String) {
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
