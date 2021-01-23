package com.github.neho4u.view

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.github.neho4u.R
import com.github.neho4u.controller.CategoryController
import com.github.neho4u.databinding.SearchEngineLayoutBinding
import com.github.neho4u.model.*
import com.github.neho4u.model.toIdWithName
import com.github.neho4u.shared.model.feedback.FeedbackFilter
import com.github.neho4u.utils.SpinnerAdapterWithInitialText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFilterDialogWrapper(
    private val parent: DrawerView,
    private val onApplyFilterCallback: (filter: FeedbackFilter) -> Unit
) {
    private val layoutBinding = SearchEngineLayoutBinding.inflate(LayoutInflater.from(parent))
    private val dialog = Dialog(parent).apply {
        setContentView(layoutBinding.root)
        setTitle(context.resources.getString(R.string.search_filer))
    }
    private val categoryController = CategoryController(parent)

    private val categories = SpinnerHolder(R.id.category_spinner, R.string.category)
    private val topics = SpinnerHolder(R.id.topic_spinner, R.string.topic)
    private val subtopics = SpinnerHolder(R.id.subtopic_spinner, R.string.subtopic)

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as IdWithName
            when (parent.id) {
                categories.spinner.id -> {
                    categories.select(item, topics)
                }
                topics.spinner.id -> {
                    topics.select(item, subtopics)
                }
                subtopics.spinner.id -> {
                    subtopics.choice = item
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

    init {
        categories.spinner.onItemSelectedListener = spinnerListener
        topics.spinner.onItemSelectedListener = spinnerListener
        subtopics.spinner.onItemSelectedListener = spinnerListener
        with(layoutBinding) {
            bNoteCancel.setOnClickListener {
                dialog.dismiss()
            }
            bNoteOk.setOnClickListener {
                onClickListener(it)
                dialog.dismiss()
            }
        }
    }

    private fun loadData() {
        GlobalScope.launch {
            val resp = categoryController.getCategories() ?: return@launch
            val (categoriesData, topicsData, subtopicsData) = resp
            withContext(Dispatchers.Main) {
                categories.updateData(categoriesData.map { it.toIdWithName() })
                topics.updateData(topicsData.map { it.toIdWithName() })
                subtopics.updateData(subtopicsData.map { it.toIdWithName() })
            }
        }
    }

    fun show() {
        dialog.show()
        loadData()
    }

    private fun onClickListener(v: View) {
        val filter = FeedbackFilter(
            categoryId = categories.choice.getChoiceId(),
            topicId = topics.choice.getChoiceId(),
            subtopicId = subtopics.choice.getChoiceId()
        )
        onApplyFilterCallback(filter)
    }

    private fun IdWithName.getChoiceId() = if (this != Placeholder) id else null

    private fun createAdapter(spinner: Spinner, defaultTextId: Int) =
        SpinnerAdapterWithInitialText(
            dialog.context,
            android.R.layout.simple_spinner_item,
            mutableListOf(),
            parent.getString(defaultTextId)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    private inner class SpinnerHolder(spinnerId: Int, defaultTextId: Int) {
        var data: List<IdWithName> = emptyList()
        var choice: IdWithName = Placeholder
        val spinner: Spinner = dialog.findViewById(spinnerId)
        val adapter = createAdapter(spinner, defaultTextId)

        fun updateData(list: List<IdWithName>) {
            data = list
            adapter.update(data)
        }

        fun select(item: IdWithName, childHolder: SpinnerHolder) {
            choice = item
            childHolder.adapter.update(
                childHolder.data.filter {
                    choice == Placeholder || it.isMyParent(choice)
                }
            )
            childHolder.spinner.setSelection(0)
            childHolder.spinner.isEnabled = choice != Placeholder
        }
    }
}
