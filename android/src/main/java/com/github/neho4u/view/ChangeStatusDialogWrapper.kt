package com.github.neho4u.view

import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.neho4u.R
import com.github.neho4u.controller.TicketController
import com.github.neho4u.databinding.ChangeStatusLayoutBinding
import com.github.neho4u.shared.model.feedback.FeedbackPriority
import com.github.neho4u.shared.model.feedback.FeedbackStatus
import com.github.neho4u.utils.SpinnerHelper
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeStatusDialogWrapper(
    private val parent: TicketView,
    private val ticketId: Long,
    private val callback: () -> Unit
) {
    private val layoutBinding = ChangeStatusLayoutBinding.inflate(LayoutInflater.from(parent))
    private val dialog = Dialog(parent).apply {
        setContentView(layoutBinding.root)
        setTitle(context.resources.getString(R.string.change_status))
    }
    private val ticketController = TicketController(parent, parent)

    private val priorities = SpinnerHolder<FeedbackPriority>(R.id.priority_spinner, R.string.choose_priority)
    private val statuses = SpinnerHolder<FeedbackStatus>(R.id.status_spinner, R.string.choose_status)

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            when (parent.id) {
                priorities.spinner.getSpinner().id -> {
                    priorities.select(parent.getItemAtPosition(position) as FeedbackPriority)
                }
                statuses.spinner.getSpinner().id -> {
                    statuses.select(parent.getItemAtPosition(position) as FeedbackStatus)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

    init {
        priorities.spinner.setOnItemSelectedListener(spinnerListener)
        statuses.spinner.setOnItemSelectedListener(spinnerListener)
        with(layoutBinding) {
            bStatusCancel.setOnClickListener {
                dialog.dismiss()
            }
            bStatusOk.setOnClickListener {
                onClickListener(it)
            }
        }
    }

    private fun loadData() {
        priorities.updateData(FeedbackPriority.values().toList())
        statuses.updateData(FeedbackStatus.values().toList())
    }

    private fun initWith(priority: FeedbackPriority?, status: FeedbackStatus?) {
        priorities.initWith(priority)
        statuses.initWith(status)
    }

    fun show(status: FeedbackStatus?, priority: FeedbackPriority?) {
        dialog.show()
        layoutBinding.bStatusOk.isEnabled = false
        loadData()
        initWith(priority, status)
        layoutBinding.bStatusOk.isEnabled = true
    }

    private fun validate(): Boolean {
        listOf<SpinnerHolder<*>>(priorities, statuses).forEach { holder ->
            if (holder.choice == null) {
                with(holder.spinner.getSpinner().selectedView as TextView) {
                    error = ""
                    setTextColor(Color.RED) // just to highlight that this is an error
                    text = holder.defaultText // changes the selected item text to this
                }
                return@validate false
            }
        }

        return true
    }

    private fun onClickListener(v: View) {
        if (!validate()) return
        GlobalScope.launch {
            try {
                priorities.choice?.let { ticketController.updateTicketPriority(ticketId, it) }
                statuses.choice?.let { ticketController.updateTicketStatus(ticketId, it) }
                withContext(Dispatchers.Main) {
                    callback()
                    dialog.dismiss()
                }
            } catch (e: Throwable) {
                parent.ticketError(parent.getString(R.string.error_conn))
            }
        }
    }

    private fun <T> createAdapter(spinner: SpinnerHelper) =
        ArrayAdapter(dialog.context, android.R.layout.simple_spinner_item, mutableListOf<T>()).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    private inner class SpinnerHolder<T : Any>(spinnerId: Int, defaultTextId: Int) {
        var data: List<T> = emptyList()
        var choice: T? = null
        val spinner: SpinnerHelper = SpinnerHelper(dialog.findViewById(spinnerId))
        val adapter = createAdapter<T>(spinner)
        val defaultText = parent.getString(defaultTextId)

        fun updateData(list: List<T>) {
            data = list
            with(adapter) {
                clear()
                addAll(list)
                notifyDataSetChanged()
            }
        }

        fun select(item: T) {
            choice = item
        }

        fun initWith(item: T?) {
            val pos = data.indexOf(item)
            if (pos >= -1) {
                choice = adapter.getItem(pos)
                spinner.setSelection(pos, true)
            }
        }
    }
}
