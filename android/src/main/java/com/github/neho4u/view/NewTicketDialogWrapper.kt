package com.github.neho4u.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.github.neho4u.R
import com.github.neho4u.controller.CategoryController
import com.github.neho4u.databinding.DialogTicketLayoutBinding
import com.github.neho4u.model.IdWithName
import com.github.neho4u.model.Placeholder
import com.github.neho4u.model.Ticket
import com.github.neho4u.model.toIdWithName
import com.github.neho4u.shared.model.feedback.FeedbackCreationDto
import com.github.neho4u.utils.IShowError
import com.github.neho4u.utils.SpinnerAdapterWithInitialText
import com.github.neho4u.utils.SpinnerHelper
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTicketDialogWrapper(
    private val context: Context,
    parent: IShowError,
    private val ticket: Ticket?,
    private val onAddFeedback: (creationDto: FeedbackCreationDto, dialog: Dialog) -> Unit
) {
    private val layoutBinding = DialogTicketLayoutBinding.inflate(LayoutInflater.from(context))
    private val dialog = Dialog(context).apply {
        setContentView(layoutBinding.root)
        setTitle(context.resources.getString(R.string.add_feedback))
    }
    private val categoryController = CategoryController(context, parent)

    private val categories = SpinnerHolder(R.id.add_category_spinner, R.string.choose_category)
    private val topics = SpinnerHolder(R.id.add_topic_spinner, R.string.choose_topic)
    private val subtopics = SpinnerHolder(R.id.add_subtopic_spinner, R.string.choose_subtopic)

    private fun initWithTicket(ticket: Ticket?) {
        layoutBinding.etTicketHead.setText(ticket?.subject ?: "")
        layoutBinding.etTicketBody.setText(ticket?.detail ?: "")
        categories.initWith(ticket?.category?.toIdWithName(), topics)
        topics.initWith(ticket?.topic?.toIdWithName(), subtopics)
        subtopics.initWith(ticket?.subtopic?.toIdWithName(), null)
    }

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as IdWithName
            when (parent.id) {
                categories.spinner.getSpinner().id -> {
                    categories.select(item, topics, subtopics)
                }
                topics.spinner.getSpinner().id -> {
                    topics.select(item, subtopics)
                }
                subtopics.spinner.getSpinner().id -> {
                    subtopics.select(item, null)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

    init {
        categories.spinner.setOnItemSelectedListener(spinnerListener)
        topics.spinner.setOnItemSelectedListener(spinnerListener)
        subtopics.spinner.setOnItemSelectedListener(spinnerListener)
        with(layoutBinding) {
            bCreateCancel.setOnClickListener {
                dialog.dismiss()
            }
            bCreateOk.setOnClickListener {
                onClickListener(it)
            }
        }
    }

    private suspend fun loadData(): Boolean {
        val resp = categoryController.getCategories() ?: return false
        val (categoriesData, topicsData, subtopicsData) = resp
        withContext(Dispatchers.Main) {
            categories.updateData(categoriesData.map { it.toIdWithName() })
            topics.updateData(topicsData.map { it.toIdWithName() })
            subtopics.updateData(subtopicsData.map { it.toIdWithName() })
        }
        return true
    }

    fun show() {
        dialog.show()
        if (ticket != null) {
            layoutBinding.newFeedback.text = context.getString(R.string.edit_feedback)
        }
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                layoutBinding.bCreateOk.isEnabled = false
            }
            if (!loadData()) {
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            } else {
                withContext(Dispatchers.Main) {
                    initWithTicket(ticket)
                    layoutBinding.bCreateOk.isEnabled = true
                }
            }
        }
    }

    private fun validate(): Boolean {
        listOf(layoutBinding.etTicketHead, layoutBinding.etTicketBody).forEach { view ->
            view.setText(view.text.trim())
            if (view.text.isEmpty()) {
                view.error = context.getString(R.string.error_not_empty)
                view.requestFocus()
                return@validate false
            }
        }

        listOf(categories, topics, subtopics).forEach { holder ->
            if (holder.choice is Placeholder) {
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
        val body = layoutBinding.etTicketBody.text.toString()
        val creationDto = FeedbackCreationDto(
            categoryId = categories.choice.id,
            topicId = topics.choice.id,
            subtopicId = subtopics.choice.id,
            header = layoutBinding.etTicketHead.text.toString(),
            comment = body
        )
        onAddFeedback(creationDto, dialog)
    }

    private fun createAdapter(spinner: SpinnerHelper, defaultTextId: Int) =
        SpinnerAdapterWithInitialText(
            dialog.context,
            android.R.layout.simple_spinner_item,
            mutableListOf(),
            context.getString(defaultTextId)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    private inner class SpinnerHolder(spinnerId: Int, defaultTextId: Int) {
        var data: List<IdWithName> = emptyList()
        var choice: IdWithName = Placeholder("")
        val spinner: SpinnerHelper = SpinnerHelper(dialog.findViewById(spinnerId))
        val adapter = createAdapter(spinner, defaultTextId)
        val defaultText = context.getString(defaultTextId)

        fun updateData(list: List<IdWithName>) {
            data = list
            adapter.update(data)
        }

        fun select(item: IdWithName, vararg childHolder: SpinnerHolder?) {
            choice = item
            var parentChoice = choice
            for (holder in childHolder) {
                holder?.let {
                    val newData = it.data.filter { id -> id.isMyParent(parentChoice) }
                    it.adapter.update(newData)
                    it.spinner.setSelection(0, true)
                    it.spinner.isEnabled = newData.isNotEmpty()
                    it.choice = it.adapter.getItem(0)
                    parentChoice = it.choice
                }
            }
        }

        fun initWith(item: IdWithName?, childHolder: SpinnerHolder?) {
            val pos = adapter.find(item)
            if (pos >= -1) {
                choice = adapter.getItem(pos + 1)
                spinner.setSelection(pos + 1, true)
            }
            childHolder?.let {
                val newData = it.data.filter { id -> id.isMyParent(choice) }
                it.adapter.update(newData)
                it.spinner.isEnabled = newData.isNotEmpty()
            }
        }
    }
}