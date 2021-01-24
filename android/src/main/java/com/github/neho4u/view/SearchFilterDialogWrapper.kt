package com.github.neho4u.view

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import com.github.neho4u.R
import com.github.neho4u.controller.CategoryController
import com.github.neho4u.databinding.SearchEngineLayoutBinding
import com.github.neho4u.model.FeedbackFilter
import com.github.neho4u.model.IdWithName
import com.github.neho4u.model.Placeholder
import com.github.neho4u.model.toIdWithName
import com.github.neho4u.utils.SpinnerAdapterWithInitialText
import com.github.neho4u.utils.SpinnerHelper
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

    private val categories = SpinnerHolder(R.id.category_spinner, R.string.choose_category)
    private val topics = SpinnerHolder(R.id.topic_spinner, R.string.choose_topic)
    private val subtopics = SpinnerHolder(R.id.subtopic_spinner, R.string.choose_subtopic)

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as IdWithName
            when (parent.id) {
                categories.spinner.getSpinner().id -> { categories.select(item, topics, subtopics) }
                topics.spinner.getSpinner().id -> { topics.select(item, subtopics) }
                subtopics.spinner.getSpinner().id -> { subtopics.select(item, null) }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

    init {
        categories.spinner.setOnItemSelectedListener(spinnerListener)
        topics.spinner.setOnItemSelectedListener(spinnerListener)
        subtopics.spinner.setOnItemSelectedListener(spinnerListener)
        with(layoutBinding) {
            bNoteCancel.setOnClickListener {
                dialog.dismiss()
            }
            bNoteOk.setOnClickListener {
                onClickListener(it)
                dialog.dismiss()
            }
            bNoteReset.setOnClickListener {
                initWithFilter(FeedbackFilter())
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

    fun show(filter: FeedbackFilter) {
        dialog.show()
        GlobalScope.launch {
            if (!loadData()) {
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            } else {
                withContext(Dispatchers.Main) {
                    initWithFilter(filter)
                }
            }
        }
    }

    private fun initWithFilter(filter: FeedbackFilter) {
        categories.initWith(filter.category, topics)
        topics.initWith(filter.topic, subtopics)
        subtopics.initWith(filter.subtopic, null)
        layoutBinding.searchView2.setText(filter.header ?: "")
    }

    private fun onClickListener(v: View) {
        val header = layoutBinding.searchView2.text.toString()
        val filter = FeedbackFilter(
            category = categories.choice.getOrNull(),
            topic = topics.choice.getOrNull(),
            subtopic = subtopics.choice.getOrNull(),
            header = if (header.isNotEmpty()) header else null
        )
        onApplyFilterCallback(filter)
    }

    private fun IdWithName.getOrNull() = if (this !is Placeholder) this else null

    private fun createAdapter(spinner: SpinnerHelper, defaultTextId: Int) =
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
        var choice: IdWithName = Placeholder("")
        val spinner: SpinnerHelper = SpinnerHelper(dialog.findViewById(spinnerId))
        val adapter = createAdapter(spinner, defaultTextId)

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
                    parentChoice = it.adapter.getItem(0)
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
